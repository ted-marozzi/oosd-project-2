import bagel.util.Point;
import bagel.util.Vector2;

import java.util.Random;

public class SuperSlicer extends Slicer {
    private static final String IMG_PATH = "res/images/superslicer.png";
    private static final int HEALTH = 1, REWARD = 15, PENALTY = 2;
    private static final double SPEED = 3.5;



    public SuperSlicer(Point start, ShadowDefend shadowDefend) {
        super(IMG_PATH, HEALTH, SPEED, REWARD, PENALTY, start, shadowDefend);
    }

    @Override
    public void spawn() {
        for(int i = 0; i < 2; i++)
        {
            Point pos = super.getPos();
            Point tmp = new Point(Math.random()-0.5, Math.random()-0.5);
            pos = pos.asVector().add(tmp.asVector().mul(getSCATTER())).asPoint();
            Slicer slicer = new RegularSlicer(pos, super.getShadowDefend());
            slicer.setPointsReached(getPointsReached());
        }

    }
}
