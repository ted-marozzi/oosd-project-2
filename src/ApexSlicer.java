import bagel.util.Point;

public class ApexSlicer extends Slicer {
    private static final String IMG_PATH = "res/images/apexslicer.png";
    private static final double HEALTH = 25, SPEED = 0.5, REWARD = 150, PENALTY = 16;

    public ApexSlicer(Point start) {
        super(IMG_PATH, HEALTH, SPEED, REWARD, PENALTY, start);
    }
}
