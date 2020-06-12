import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.List;


public abstract class Tower {

    public boolean isBuying = false;
    private final Image img;
    private final int price;
    private Point pos;

    protected Tower(Point pos, String imgPath, int price) {
        this.img = new Image(imgPath);
        this.pos = pos;
        this.price = price;
    }

    // Gets a bounding box at the pos of the tower
    public Rectangle getBoundingBox()
    {
        return img.getBoundingBoxAt(pos);
    }

    // Checks if the tower was clicked on
    public boolean wasClicked(Input input)
    {
        return input.getMouseX() >= pos.x - img.getWidth() / 2 && input.getMouseX() <= pos.x + img.getWidth() / 2
                && input.getMouseY() >= pos.y - img.getWidth() / 2 && input.getMouseY() <= pos.y + img.getWidth() / 2;
    }


    // Overloaded draw method
    public void draw(int x, int y)
    {
        img.draw(x, y);
    }

    public void draw()
    {
        img.draw(pos.x, pos.y);
    }

    public void draw(DrawOptions drawOptions)
    {
        img.draw(pos.x, pos.y, drawOptions);
    }

    public abstract Tower create(Point pos);

    public abstract void update(List<Slicer> slicerList, ShadowDefend shadowDefend);

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

    public Point getPos() {
        return pos;
    }



}
