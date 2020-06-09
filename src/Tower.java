import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;

import java.util.List;


public abstract class Tower {

    public boolean isBuying = false;
    private final Image img;
    private final int price;
    private Point pos;
    private DrawOptions drawOptions;



    protected Tower(Point pos, String imgPath, int price)
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

    public void draw(int x, int y)
    {
        img.draw(x, y);
    }

    public void draw()
    {

        img.draw(pos.x, pos.y);
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public Point getPos() {
        return pos;
    }

    public void draw(DrawOptions drawOptions)
    {
        img.draw(pos.x, pos.y, drawOptions);
    }


    public abstract Tower create(Point pos);


    public abstract void update(List<Slicer> slicerList, ShadowDefend shadowDefend);
}
