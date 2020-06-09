import bagel.util.Point;


public class RegularSlicer extends Slicer {

    private static final String IMG_PATH = "res/images/slicer.png";
    private static final int HEALTH = 1, REWARD = 2, PENALTY = 1;
    private static final double SPEED = 2;

    public RegularSlicer(Point start, ShadowDefend shadowDefend) {
        super(IMG_PATH, HEALTH, SPEED, REWARD, PENALTY, start, shadowDefend);
    }

    @Override
    public void spawn() {
        return;
    }
}
