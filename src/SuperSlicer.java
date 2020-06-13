import bagel.util.Point;

/**
 * A type of slicer.
 */
public class SuperSlicer extends Slicer {
    private static final String IMG_PATH = "res/images/superslicer.png";
    private static final int HEALTH = 1, REWARD = 15, PENALTY = 2;
    private static final double SPEED = 1.5;
    private static final int CHILDREN_TO_SPAWN = 2;

    /**
     * @param start The start of the SuperSlicer.
     */
    public SuperSlicer(Point start) {
        super(IMG_PATH, HEALTH, SPEED, REWARD, PENALTY, start);
    }

    /**
     * @param shadowDefend The game.
     */
    // Spawns 2 regular slicers
    @Override
    public void spawn(ShadowDefend shadowDefend) {
        for(int i = 0; i < CHILDREN_TO_SPAWN; i++)
        {
            Point pos = super.getPos();
            Point tmp = new Point(Math.random()-getHALF_PX(), Math.random()-getHALF_PX());
            pos = pos.asVector().add(tmp.asVector().mul(getSCATTER())).asPoint();
            Slicer slicer = new RegularSlicer(pos);
            slicer.setPointsReached(getPointsReached());
            shadowDefend.addSlicer(slicer);
        }

    }
}
