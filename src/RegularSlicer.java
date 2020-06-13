import bagel.util.Point;

/**
 * Most basic slicer, doesn't spawn children.
 */
// Standard slicer
public class RegularSlicer extends Slicer {

    private static final String IMG_PATH = "res/images/slicer.png";
    private static final int HEALTH = 1, REWARD = 2, PENALTY = 1;
    private static final double SPEED = 2;

    /**
     * @param start Starting location of the slicer.
     */
    public RegularSlicer(Point start) {
        super(IMG_PATH, HEALTH, SPEED, REWARD, PENALTY, start);
    }

    /**
     * @param shadowDefend The game to spawn into.
     */
    // Doesn't spawn upon death
    @Override
    public void spawn(ShadowDefend shadowDefend) {
    }
}
