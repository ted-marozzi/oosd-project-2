import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.List;

// Enemy class, if we have multiple classes,
//      I will make a new slicer class that extends a base enemy class
public class Slicer {
    private Image player;
    private Point currentPos;
    private int pointsReached = 0;
    private static int numEnemies = 0;

    private DrawOptions drawOptions;
    private boolean isAlive = true;

    public Slicer(String imgPath, Point start) // Number one
    {
        this.player = new Image(imgPath);

        moveTo(start);
        drawOptions = new DrawOptions();
        numEnemies++;
    }

    // Moves player to PlayerPoint, with draw options setting rotation
    // Moves player when given a rotation argument
    public void moveTo(Point playerPoint, DrawOptions drawOptions) {
        this.currentPos = playerPoint;
        player.draw(currentPos.x, currentPos.y, drawOptions);

    }
    // Moves player to given point when no rotation is applied
    public void moveTo(Point playerPoint) {
        this.currentPos = playerPoint;
        player.draw(currentPos.x, currentPos.y);

    }

    // Adds the unit vector direction to current point
    private Point calcNextPos(Vector2 direction, int timeScale)
    {
        return this.currentPos.asVector().add(direction.mul(timeScale)).asPoint();
    }

    // Calculates the unit direction vector
    private Vector2 calcDirectionVec(List<Point> polyLines, int timeScale)
    {

        if(this.currentPos.distanceTo(polyLines.get(this.pointsReached)) < (double)timeScale/2)
        {
            // Increase the points reached by the player by 1
            this.pointsReached = this.pointsReached+1;
        }
        return ((polyLines.get(this.pointsReached)).asVector().sub(this.currentPos.asVector())).normalised();
    }
    // Updates an individua enemy dependant on the position
    public void update(int timeScale, List<Point> polyLines)
    {
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
        }
    }

    public boolean isAlive() {
        return isAlive;
    }


    public static int getNumEnemies() {
        return Slicer.numEnemies;
    }

    public static void setNumEnemies(int numEnemies) {
        Slicer.numEnemies = numEnemies;
    }


}
