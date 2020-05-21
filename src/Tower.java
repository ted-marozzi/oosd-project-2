import bagel.Image;
import bagel.Input;

public class Tower {

    private static Image img;
    private static int cost;

    public Tower(String imgPath, int cost)
    {
        this.img = new Image(imgPath);
        this.cost = cost;

        ShadowDefend.getInstance().addTower(this);
    }

    public void buyTower()
    {
        ShadowDefend.getInstance().minusCash(this.cost);

    }

    public void drawTowerAtMouse(Input input)
    {
        this.img.draw(input.getMouseX(), input.getMouseY());

    }

}
