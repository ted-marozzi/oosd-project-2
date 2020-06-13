import bagel.util.Point;

/**
 * A type of Ground Tower, stronger than the Tank.
 */
// Type of ground tower
public class SuperTank extends GroundTower {

    private static final String IMG_PATH = "res/images/supertank.png";
    private static final int PRICE = 600;
    private static final int RANGE = 150;
    private static final int DAMAGE = 3;
    private static final int COOLDOWN = 500;
    private static final String PROJECTILE_PATH = "res/images/supertank_projectile.png";

    /**
     * @param pos The position of the SuperTank
     */
    public SuperTank(Point pos) {
        super(pos, IMG_PATH, PRICE, RANGE, DAMAGE, COOLDOWN, PROJECTILE_PATH);
    }


    /**
     * @param obj The object to be compared to this.
     * @return A boolean determining if they are equal.
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * @param pos The position to create the SuperTank at.
     * @return The actual Tank.
     */
    @Override
    public SuperTank create(Point pos)
    {
        return new SuperTank(pos);
    }



}
