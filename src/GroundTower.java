import bagel.util.Point;

public abstract class GroundTower extends Tower
{

    protected GroundTower(String imgPath, int price, Point pos)
    {
        super(imgPath, price, pos);
    }

    @Override
    public void update() {

    }
}
