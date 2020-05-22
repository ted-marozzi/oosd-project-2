import bagel.util.Point;


public class SuperTank extends GroundTower {

    private static final String IMG_PATH = "res/images/supertank.png";
    private static final int PRICE = 600;

    public SuperTank() {
        super(IMG_PATH, PRICE, new Point(0,0));
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public SuperTank create()
    {
        return new SuperTank();
    }
}
