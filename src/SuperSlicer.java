import bagel.util.Point;

public class SuperSlicer extends Slicer {
    private static final String IMG_PATH = "res/images/superslicer.png";
    private static final double HEALTH = 1, SPEED = 3.5, REWARD = 15, PENALTY = 2;
    public SuperSlicer(Point start) {
        super();
        setSlicerImg(IMG_PATH);
        setHealth(HEALTH);
        setSpeed(SPEED);
        setPenalty(PENALTY);
        moveTo(start);
    }
}
