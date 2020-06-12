import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Enemy class, if we have multiple classes,
//      I will make a new slicer class that extends a base enemy class
public abstract class Slicer {

    private final double speed;
    private int reward, health, penalty;


    private final Image slicerImg;
    private Point pos;
    private int pointsReached = 0;


    private final DrawOptions drawOptions;
    private boolean isAlive = true;
    private static final int SCATTER = 10;
    private Vector2 directionVec;


    protected Slicer(String imgPath, int health, double speed, int reward, int penalty, Point start) {
        drawOptions = new DrawOptions();
        this.slicerImg = new Image(imgPath);
        this.health = health;
        this.speed = speed;
        this.penalty = penalty;
        this.reward = reward;
        moveTo(start);
    }


    // Moves player to PlayerPoint, with draw options setting rotation
    // Moves player when given a rotation argument
    public void moveTo(Point playerPoint, DrawOptions drawOptions) {
        this.pos = playerPoint;
        slicerImg.draw(pos.x, pos.y, drawOptions);

    }

    // Moves player to given point when no rotation is applied
    public void moveTo(Point playerPoint) {
        this.pos = playerPoint;
        slicerImg.draw(pos.x, pos.y);

    }

    public void dealDamage(int damage, ShadowDefend shadowDefend) {
        this.health = health - damage;
        if (health <= 0) {
            spawn(shadowDefend);
            isAlive = false;
            shadowDefend.addCash(this.reward);
        }
    }


    // Adds the unit vector direction to current point
    private Point calcNextPos(Vector2 direction, double timeScale) {
        return this.pos.asVector().add(direction.mul(speed * timeScale)).asPoint();
    }

    // Calculates the unit direction vector
    private Vector2 calcPolyDirectionVec(List<Point> polyLines, double timeScale) {
        int nextPoint;
        if (this.pos.distanceTo(polyLines.get(this.pointsReached)) < timeScale * speed / 2) {
            // Increase the points reached by the player by 1
            this.pointsReached = this.pointsReached + 1;
        }
        if (pointsReached == polyLines.size()) {
            nextPoint = pointsReached - 1;
        } else {
            nextPoint = pointsReached;
        }
        return ((polyLines.get(nextPoint)).asVector().sub(this.pos.asVector())).normalised();
    }

    // Updates an individual enemy dependant on the position
    public void update(double timeScale, ShadowDefend shadowDefend) {
        List<Point> polyLines = shadowDefend.getCurrentLevel().getPolyLines();

        if (this.pointsReached < polyLines.size()) {
            directionVec = calcPolyDirectionVec(polyLines, timeScale);
            move(directionVec, timeScale);

        } else {
            isAlive = false;
            shadowDefend.setLives(shadowDefend.getLives() - this.penalty);

        }
    }

    private void move(Vector2 directionVec, double timeScale) {
        Point nextPos = calcNextPos(directionVec, timeScale);

        // Calc rotation
        double rotation = Math.atan(directionVec.y / directionVec.x);
        // As atan only guaranteed to work for positive x
        if (directionVec.asPoint().x < 0)
            rotation = rotation - Math.PI;

        this.drawOptions.setRotation(rotation);
        // Move enemy
        this.moveTo(nextPos, this.drawOptions);

    }


    public static void update(ShadowDefend shadowDefend) {
        // Every slicer perform the update and remove dead enemies
        // Allows for removing enemies if player defeats them also
        Iterator<Slicer> it = shadowDefend.getSlicerList().iterator();
        while (it.hasNext()) {
            Slicer slicer = it.next();
            slicer.update(ShadowDefend.getTimeScale(), shadowDefend);

            if (!slicer.isAlive) {
                it.remove();
            }
        }
    }

    public Point getPos() {
        return pos;
    }


    public abstract void spawn(ShadowDefend shadowDefend);

    public int getPointsReached() {
        return pointsReached;
    }

    public static int getSCATTER() {
        return SCATTER;
    }

    public void setPointsReached(int pointsReached) {
        this.pointsReached = pointsReached;
    }
}
