import bagel.Image;
import bagel.Input;

import java.util.ArrayList;

public abstract class GroundTower
{
    ArrayList<GroundTower> towerList = new ArrayList<>();
    private Image img;
    private int price;

    protected GroundTower(String imgPath, int price)
    {
        img = new Image(imgPath);

        this.price = price;
        towerList.add(this);
    }


    public void drawImgAtMouse(Input input)
    {
        this.img.draw(input.getMouseX(), input.getMouseY());
    }

    public void drawImg(int x, int y)
    {
        this.img.draw(x, y);
    }

}
