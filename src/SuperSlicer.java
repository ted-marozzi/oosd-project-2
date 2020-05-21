import bagel.util.Point;

public class SuperSlicer extends Slicer {
    private static final String IMG_PATH = "res/images/superslicer.png";
    private static final int HEALTH = 1, REWARD = 15, PENALTY = 2;
    private static final double SPEED = 3.5;

    public SuperSlicer(Point start) {
        super(IMG_PATH, HEALTH, SPEED, REWARD, PENALTY, start);
    }
}
