import bagel.util.Point;
// TODO: check if data types can be ints
public class MegaSlicer extends Slicer {
    private static final String IMG_PATH = "res/images/megaslicer.png";
    private static final double HEALTH = 2, SPEED = 1, REWARD = 10, PENALTY = 4;

    public MegaSlicer(Point start) {
        super(IMG_PATH, HEALTH, SPEED, REWARD, PENALTY, start);
    }
}
