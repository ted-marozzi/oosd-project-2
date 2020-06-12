import bagel.util.Point;

// Type of ground tower
public class SuperTank extends GroundTower {

    private static final String IMG_PATH = "res/images/supertank.png";
    private static final int PRICE = 600;
    private static final int RANGE = 150;
    private static final int DAMAGE = 3;
    private static final int COOLDOWN = 500;
    private static final String PROJECTILE_PATH = "res/images/supertank_projectile.png";

    public SuperTank(Point pos) {
        super(pos, IMG_PATH, PRICE, RANGE, DAMAGE, COOLDOWN, PROJECTILE_PATH);
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public SuperTank create(Point pos)
    {
        return new SuperTank(pos);
    }



}
