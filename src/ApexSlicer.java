import bagel.util.Point;

public class ApexSlicer extends Slicer {
    private static final String IMG_PATH = "res/images/apexslicer.png";
    private static final int HEALTH = 25, REWARD = 150, PENALTY = 16;
    private static final double SPEED = 0.5;

    public ApexSlicer(Point start) {
        super(IMG_PATH, HEALTH, SPEED, REWARD, PENALTY, start);
    }

    @Override
    public void spawn(ShadowDefend shadowDefend) {
        for (int i = 0; i < 4; i++) {
            Point pos = super.getPos();
            Point tmp = new Point(Math.random() - 0.5, Math.random() - 0.5);
            pos = pos.asVector().add(tmp.asVector().mul(getSCATTER())).asPoint();
            Slicer slicer = new MegaSlicer(pos);
            slicer.setPointsReached(getPointsReached());
            shadowDefend.addSlicer(slicer);

        }

    }
}
