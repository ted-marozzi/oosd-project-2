import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;

public class Bomb
{

    private final Image img;
    private final Point pos;
    private StopWatch stopWatch;
    private static final int DET_TIME = 2000;
    private static final String IMG_PATH = "res/images/explosive.png";
    private static final int DAMAGE = 500;
    private static final int RADIUS = 200;



    public Bomb(Point pos)
    {
        this.img = new Image(IMG_PATH);
        this.pos = pos;
        this.stopWatch = new StopWatch();

    }

    public boolean drop(List<Slicer> slicerList)
    {
        img.draw(this.pos.x, this.pos.y);
        if(stopWatch.lap()  >= DET_TIME )
        {
            // explode

            slicerList.removeIf(slicer -> slicer.getPos().distanceTo(this.pos) <= RADIUS);


            return true;
        }
        return false;

    }

}
