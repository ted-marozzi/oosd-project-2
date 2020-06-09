import bagel.util.Point;
// TODO: check if data types can be ints
public class MegaSlicer extends Slicer {
    private static final String IMG_PATH = "res/images/megaslicer.png";
    private static final int HEALTH = 2, REWARD = 10, PENALTY = 4;
    private static final double SPEED = 1;

    public MegaSlicer(Point start, ShadowDefend shadowDefend) {
        super(IMG_PATH, HEALTH, SPEED, REWARD, PENALTY, start, shadowDefend);
    }

    @Override
    public void spawn() {
        Point pos = super.getPos();
        for(int i = 0; i < 2; i++)
        {

            Point tmp = new Point(Math.random()-0.5, Math.random()-0.5);
            pos = pos.asVector().add(tmp.asVector().mul(getSCATTER())).asPoint();
            Slicer slicer = new SuperSlicer(pos, getShadowDefend());
            slicer.setPointsReached(getPointsReached());

        }

    }
}
