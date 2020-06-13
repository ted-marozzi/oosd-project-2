import bagel.Image;
import bagel.util.Point;

import java.util.*;

/**
 * Bomb class created by AirSupport plane.
 */
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


    /**
     * @param pos to create the bomb at.
     */
    // Constructor
    public Bomb(Point pos)
    {
        this.img = new Image(IMG_PATH);
        this.pos = pos;
        this.stopWatch = new StopWatch();
    }

    /**
     * @param slicerList list of slicers.
     * @param shadowDefend game to drop bombs into.
     * @return boolean determines if the bomb is exploded or not.
     */
    // Drops the bomb returns isExploded
    public boolean drop(List<Slicer> slicerList, ShadowDefend shadowDefend)
    {
        img.draw(this.pos.x, this.pos.y);
        if(stopWatch.lapMS()  >= DET_TIME )
        {
            // explode, iterator used because slicers get removed from list

            try {
                for (Slicer slicer : slicerList) {

                    if (slicer.getPos().distanceTo(this.pos) <= RADIUS) {
                        slicer.dealDamage(DAMAGE, shadowDefend);
                    }
                }
            }
            catch (ConcurrentModificationException ignored)
            {
                // Ignore because slicer in question has been deleted hence this is safe.
            }


            return true;
        }
        return false;

    }

}
