import bagel.util.Point;

public class AirSupport extends Tower {

    private static final String IMG_PATH = "res/images/airsupport.png";
    private static final int PRICE = 500;


    public AirSupport() {
        super(IMG_PATH, PRICE, new Point(0,0));
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }


    public AirSupport create()
    {
        return new AirSupport();
    }

    @Override
    public void update() {

    }
}
