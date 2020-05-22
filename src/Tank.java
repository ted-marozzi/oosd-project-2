import bagel.util.Point;

public class Tank extends GroundTower {

    private static final String IMG_PATH = "res/images/tank.png";
    private static final int PRICE = 250;

    public Tank() {
        super(IMG_PATH, PRICE, new Point(0,0));
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public Tank create()
    {
        return new Tank();
    }

}
