import bagel.util.Point;

/**
 * Tank class is a type of GroundTower.
 */
// Regular tank
public class Tank extends GroundTower {

    private static final String IMG_PATH = "res/images/tank.png";
    private static final int PRICE = 250;
    private static final int RANGE = 100;
    private static final int DAMAGE = 1;
    private static final int COOLDOWN = 1000;
    private static final String PROJECTILE_PATH = "res/images/tank_projectile.png";

    /**
     * @param pos The position of the Tank to be created.
     */
    public Tank(Point pos) {
        super(pos, IMG_PATH, PRICE,  RANGE, DAMAGE, COOLDOWN, PROJECTILE_PATH);
    }

    /**
     * @param obj The object to be compared to.
     * @return The boolean if the are the same or not.
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * @param pos The position to create the tank at.
     * @return The tank created.
     */
    @Override
    public Tank create(Point pos)
    {
        return new Tank(pos);
    }

}
