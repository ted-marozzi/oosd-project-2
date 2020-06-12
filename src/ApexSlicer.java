import bagel.util.Point;

// Type of slicer
public class ApexSlicer extends Slicer {
    private static final String IMG_PATH = "res/images/apexslicer.png";
    private static final int HEALTH = 25, REWARD = 150, PENALTY = 16;
    private static final double SPEED = 0.5;
    private static final int CHILDREN_TO_SPAWN = 4;

    public ApexSlicer(Point start) {
        super(IMG_PATH, HEALTH, SPEED, REWARD, PENALTY, start);
    }

    @Override
    public void spawn(ShadowDefend shadowDefend) {
        // Spawns children slicers in this case 4 Mega slicers
        for (int i = 0; i < CHILDREN_TO_SPAWN; i++) {
            // Get current pos
            Point pos = super.getPos();
            // Used to make the slicers not stack on each other
            Point tmp = new Point(Math.random() - 0.5, Math.random() - 0.5);
            pos = pos.asVector().add(tmp.asVector().mul(getSCATTER())).asPoint();
            // Craete the new slicers
            Slicer slicer = new MegaSlicer(pos);
            // Update the number of points reached
            slicer.setPointsReached(getPointsReached());
            // Add to list
            shadowDefend.addSlicer(slicer);
        }
    }
}
