import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.List;


/**
 * Abstract tower class
 */
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

    /**
     * @return A bounding box at the current position of the Tower.
     */
    // Gets a bounding box at the pos of the tower
    public Rectangle getBoundingBox()
    {
        return img.getBoundingBoxAt(pos);
    }

    /**
     * @param input The user input.
     * @return A boolean that is true if this was clicked.
     */
    // Checks if the tower was clicked on
    public boolean wasClicked(Input input)
    {
        return input.getMouseX() >= pos.x - img.getWidth() / 2 && input.getMouseX() <= pos.x + img.getWidth() / 2
                && input.getMouseY() >= pos.y - img.getWidth() / 2 && input.getMouseY() <= pos.y + img.getWidth() / 2;
    }


    /**
     * @param x The x position to draw at.
     * @param y The y position to draw at.
     */
    // Overloaded draw method
    public void draw(int x, int y)
    {
        img.draw(x, y);
    }

    /**
     * Draw at current position
     */
    public void draw()
    {
        img.draw(pos.x, pos.y);
    }

    /**
     * @param drawOptions The options to draw the Tower with.
     */
    public void draw(DrawOptions drawOptions)
    {
        img.draw(pos.x, pos.y, drawOptions);
    }

    /**
     * @param pos The position to draw the Tower at.
     * @return The tower object.
     */
    public abstract Tower create(Point pos);

    /**
     * @param slicerList The list of slicers.
     * @param shadowDefend The game.
     */
    public abstract void update(List<Slicer> slicerList, ShadowDefend shadowDefend);

    /**
     * @return The price of the tower.
     */
    public int getPrice() {
        return price;
    }

    /**
     * @param isBuying The boolean to check if the tower is being bought.
     */
    public void setIsBuying(boolean isBuying) {
        this.isBuying = isBuying;
    }

    /**
     * @return The boolean to check if the tower is being bought.
     */
    public boolean getIsBuying()
    {
        return isBuying;
    }

    /**
     * @param pos The position to set the tower at.
     */
    public void setPos(Point pos) {
        this.pos = pos;
    }

    /**
     * @return The position the tower is set at.
     */
    public Point getPos() {
        return pos;
    }



}
