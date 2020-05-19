import bagel.Image;
import bagel.Input;

public class SuperTank extends GroundTower {
    private static final String imgPath = "res/images/supertank.png";
    private static Image tankImg;
    private static int price = 600;

    protected SuperTank(String imgPath) {
        super(imgPath, price);
    }

    public static void drawTank(int x, int y)
    {
        tankImg = new Image(imgPath);
        tankImg.draw(x, y);
    }



    public static int getPrice()
    {
        return price;
    }
}
