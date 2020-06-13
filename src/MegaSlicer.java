import bagel.util.Point;

/**
 * Type of slicer.
 */
// Slicer type
public class MegaSlicer extends Slicer {
    private static final String IMG_PATH = "res/images/megaslicer.png";
    private static final int HEALTH = 2, REWARD = 10, PENALTY = 4;
    private static final double SPEED = 1.5;
    private static final int CHILDREN_TO_SPAWN = 2;


    /**
     * @param start The starting location of the mega slicer.
     */
    public MegaSlicer(Point start) {
        super(IMG_PATH, HEALTH, SPEED, REWARD, PENALTY, start);
    }

    /**
     * @param shadowDefend The game to spawn the slicers into.
     */
    // Spawns children slicers in this case 2 Super slicers
    @Override
    public void spawn(ShadowDefend shadowDefend) {
        Point pos = super.getPos();
        for (int i = 0; i < CHILDREN_TO_SPAWN; i++) {

            Point tmp = new Point(Math.random() - getHALF_PX(), Math.random() - getHALF_PX());
            pos = pos.asVector().add(tmp.asVector().mul(getSCATTER())).asPoint();
            Slicer slicer = new SuperSlicer(pos);
            slicer.setPointsReached(getPointsReached());

            shadowDefend.addSlicer(slicer);
        }

    }
}
