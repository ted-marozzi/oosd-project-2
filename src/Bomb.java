import bagel.Image;
import bagel.util.Point;

import java.util.Iterator;
import java.util.List;

// Dropped by AirSupport
public class Bomb
{

    private final Image img;
    private final Point pos;
    private final StopWatch stopWatch;
    private static final int DET_TIME = 2000;
    private static final String IMG_PATH = "res/images/explosive.png";
    private static final int DAMAGE = 500;
    private static final int RADIUS = 200;


    // Constructor
    public Bomb(Point pos)
    {
        this.img = new Image(IMG_PATH);
        this.pos = pos;
        this.stopWatch = new StopWatch();
    }

    // Drops the bomb returns isExploded
    public boolean drop(List<Slicer> slicerList, ShadowDefend shadowDefend)
    {
        img.draw(this.pos.x, this.pos.y);
        if(stopWatch.lapMS()  >= DET_TIME )
        {
            // explode, iterator used because slicers get removed from list
            Iterator<Slicer> it = slicerList.iterator();
            while(it.hasNext())
            {
                Slicer slicer = it.next();
                if(slicer.getPos().distanceTo(this.pos) <= RADIUS) {
                    slicer.dealDamage(DAMAGE, shadowDefend);
                }
            }

            return true;
        }
        return false;

    }

}
