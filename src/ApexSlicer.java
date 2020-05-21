import bagel.util.Point;

public class ApexSlicer extends Slicer {
    private static final String IMG_PATH = "res/images/apexslicer.png";
    private static final int HEALTH = 25, REWARD = 150, PENALTY = 16;
    private static final double SPEED = 0.5;

    public ApexSlicer(Point start) {
        super(IMG_PATH, HEALTH, SPEED, REWARD, PENALTY, start);
    }
}
