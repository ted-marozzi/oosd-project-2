import bagel.util.Point;


public class RegularSlicer extends Slicer {

    private static final String IMG_PATH = "res/images/slicer.png";
    private static final double HEALTH = 1, SPEED = 2, REWARD = 2, PENALTY = 1;


    public RegularSlicer(Point start) {
        super(IMG_PATH, HEALTH, SPEED, REWARD, PENALTY, start);
    }
}
