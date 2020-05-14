import bagel.util.Point;

public class MegaSlicer extends Slicer {
    private static final String IMG_PATH = "res/images/megaslicer.png";
    private static final double HEALTH = 2, SPEED = 1, REWARD = 10, PENALTY = 4;

    public MegaSlicer(Point start) {
        super();
        setSlicerImg(IMG_PATH);
        setHealth(HEALTH);
        setSpeed(SPEED);
        setPenalty(PENALTY);
        moveTo(start);
    }
}
