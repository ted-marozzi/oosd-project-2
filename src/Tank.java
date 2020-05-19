import bagel.Image;
import bagel.Input;

public class Tank extends GroundTower {

    private static final String imgPath = "res/images/tank.png";
    private static int price = 250;

    public Tank(Input input)
    {
        super(imgPath, price);

    }

    public static void drawTank(int x, int y)
    {
        Image tankImg = new Image(imgPath);
        tankImg.draw(x, y);
    }


    public static int getPrice()
    {
        return price;
    }




}
