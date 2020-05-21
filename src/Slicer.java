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


    private double  speed;
    private int reward, health, penalty;


    private Image slicerImg;
    private Point currentPos;
    private int pointsReached = 0;


    private final DrawOptions drawOptions;
    private boolean isAlive = true;


    protected Slicer(String imgPath, int health, double speed, int reward, int penalty, Point start)
    {
        drawOptions = new DrawOptions();
        this.slicerImg = new Image(imgPath);
        this.health = health;
        this.speed = speed;
        this.penalty = penalty;
        this.reward = reward;
        moveTo(start);
        ShadowDefend.getInstance().addSlicer(this);
    }



    // Moves player to PlayerPoint, with draw options setting rotation
    // Moves player when given a rotation argument
    public void moveTo(Point playerPoint, DrawOptions drawOptions) {
        this.currentPos = playerPoint;
        slicerImg.draw(currentPos.x, currentPos.y, drawOptions);

    }

    // Moves player to given point when no rotation is applied
    public void moveTo(Point playerPoint) {
        this.currentPos = playerPoint;
        slicerImg.draw(currentPos.x, currentPos.y);

    }

    // Adds the unit vector direction to current point
    private Point calcNextPos(Vector2 direction, double timeScale)
    {
        return this.currentPos.asVector().add(direction.mul(speed*timeScale)).asPoint();
    }

    // Calculates the unit direction vector
    private Vector2 calcDirectionVec(List<Point> polyLines, double timeScale)
    {

        if(this.currentPos.distanceTo(polyLines.get(this.pointsReached)) < timeScale *speed/2)
        {
            // Increase the points reached by the player by 1
            this.pointsReached = this.pointsReached+1;
        }
        return ((polyLines.get(this.pointsReached)).asVector().sub(this.currentPos.asVector())).normalised();
    }
    // Updates an individual enemy dependant on the position
    public void update(double timeScale)
    {

        List<Point> polyLines = ShadowDefend.getInstance().getCurrentLevel().getPolyLines();
        if(this.pointsReached < polyLines.size()-1)
        {
            Vector2 directionVec = calcDirectionVec(polyLines, timeScale);

            Point nextPos = calcNextPos(directionVec, timeScale);

            // Calc rotation
            double rotation = Math.atan(directionVec.y/directionVec.x);
            // As atan only guaranteed to work for positive x
            if(directionVec.asPoint().x < 0)
                rotation = rotation-Math.PI;

            this.drawOptions.setRotation(rotation);
            // Move enemy
            this.moveTo(nextPos, this.drawOptions);

        }
        else
        {
            isAlive = false;
            ShadowDefend.getInstance().setLives((int) (ShadowDefend.getInstance().getLives() - this.penalty));

        }
    }


    public static void update() {
        // Every slicer perform the update and remove dead enemies
        // Allows for removing enemies if player defeats them also
        Iterator<Slicer> it = ShadowDefend.getInstance().getSlicerList().iterator();
        while (it.hasNext()) {
            Slicer slicer = it.next();
            slicer.update(ShadowDefend.getTimeScale());

            if (!slicer.isAlive) {
                it.remove();
            }
        }
    }

//    protected void setHealth(double health) {
//        this.health = health;
//    }
//
//    protected void setPenalty(double penalty)
//    {
//        this.penalty = penalty;
//    }
//
//
//    protected void setSlicerImg(String imgPath)
//    {
//        this.slicerImg = new Image(imgPath);
//    }
//
//    protected void setSpeed(double speed)
//    {
//        this.speed = speed;
//    }
//


}
