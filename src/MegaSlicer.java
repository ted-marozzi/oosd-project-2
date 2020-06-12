import bagel.util.Point;

// Slicer type
public class MegaSlicer extends Slicer {
    private static final String IMG_PATH = "res/images/megaslicer.png";
    private static final int HEALTH = 2, REWARD = 10, PENALTY = 4;
    private static final double SPEED = 1;
    private static final int CHILDREN_TO_SPAWN = 2;

    public MegaSlicer(Point start) {
        super(IMG_PATH, HEALTH, SPEED, REWARD, PENALTY, start);
    }

    // Spawns children slicers in this case 2 Super slicers
    @Override
    public void spawn(ShadowDefend shadowDefend) {
        Point pos = super.getPos();
        for (int i = 0; i < CHILDREN_TO_SPAWN; i++) {

            Point tmp = new Point(Math.random() - 0.5, Math.random() - 0.5);
            pos = pos.asVector().add(tmp.asVector().mul(getSCATTER())).asPoint();
            Slicer slicer = new SuperSlicer(pos);
            slicer.setPointsReached(getPointsReached());

            shadowDefend.addSlicer(slicer);
        }

    }
}
