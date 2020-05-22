import bagel.Image;
import bagel.Input;
import bagel.util.Point;


public abstract class Tower {

    public boolean isBuying = false;
    private final Image img;
    private final int price;
    private Point pos;



    protected Tower(String imgPath, int price, Point pos)
    {
        this.img = new Image(imgPath);
        this.price = price;
        this.pos = pos;

    }

    public boolean wasClicked(Input input)
    {
        return input.getMouseX() >= pos.x - img.getWidth() / 2 && input.getMouseX() <= pos.x + img.getWidth() / 2
                && input.getMouseY() >= pos.y - img.getWidth() / 2 && input.getMouseY() <= pos.y + img.getWidth() / 2;
    }

    public int getPrice() {
        return price;
    }


    public void setIsBuying(boolean isBuying) {
        this.isBuying = isBuying;
    }

    public boolean getIsBuying()
    {
        return isBuying;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public void draw(int x, int y)
    {

        this.setPos(new Point(x, y));
        img.draw(x, y);
    }

    public void draw()
    {

        img.draw(pos.x, pos.y);
    }

    public abstract Tower create();


    public abstract void update();
}
