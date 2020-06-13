import bagel.util.Point;

/**
 * Type of slicer.
 */
// Type of slicer
public class ApexSlicer extends Slicer {
    private static final String IMG_PATH = "res/images/apexslicer.png";
    private static final int HEALTH = 25, REWARD = 150, PENALTY = 16;
    private static final double SPEED = 0.75;
    private static final int CHILDREN_TO_SPAWN = 4;


    /**
     * @param start Starting point of the Apex slicer
     */
    public ApexSlicer(Point start) {
        super(IMG_PATH, HEALTH, SPEED, REWARD, PENALTY, start);
    }

    /**
     * @param shadowDefend The game to spawn the children in.
     */
    @Override
    public void spawn(ShadowDefend shadowDefend) {
        // Spawns children slicers in this case 4 Mega slicers
        for (int i = 0; i < CHILDREN_TO_SPAWN; i++) {
            // Get current pos
            Point pos = super.getPos();
            // Used to make the slicers not stack on each other
            Point tmp = new Point(Math.random() - getHALF_PX(), Math.random() - getHALF_PX());
            pos = pos.asVector().add(tmp.asVector().mul(getSCATTER())).asPoint();
            // Creates the new slicers
            Slicer slicer = new MegaSlicer(pos);
            // Update the number of points reached
            slicer.setPointsReached(getPointsReached());
            // Add to list
            shadowDefend.addSlicer(slicer);
        }
    }
}
