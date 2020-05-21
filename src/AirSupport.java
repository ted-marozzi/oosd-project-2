import bagel.Image;

public class AirSupport extends Tower {

    private static final String IMG_PATH = "res/images/airsupport.png";
    private static final int PRICE = 500;

    public AirSupport(String imgPath, int cost) {
        super(imgPath, cost);
    }

    public static void draw(int x, int y)
    {
        Image airSupportImg = new Image(IMG_PATH);
        airSupportImg.draw(x, y);
    }

    public static int getPrice() {
        return PRICE;
    }
}
