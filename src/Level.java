import bagel.Input;
import bagel.Keys;
import bagel.map.TiledMap;
import bagel.util.Point;
import static bagel.Window.close;

import java.util.List;
// Class which holds information specific to a level/map
public class Level
{
    // Variables
    private TiledMap map;
    private List<Point> polyLines;
    private Point start;
    private boolean isWave = false;

    // Constructor
    public Level(String levelPath)
    {
        this.map = new TiledMap(levelPath);
        polyLines = map.getAllPolylines().get(0);
        start = polyLines.get(0);
    }

    // Initiates a wave
    public void beginWave(Input input)
    {
        if(input.wasPressed(Keys.S))
        {
            if(!this.getIsWave())
            {
                ShadowDefend.addSlicer(start);
            }
            this.isWave = true;
        }
    }

    // Ends a wave
    public void endWave()
    {
        if(ShadowDefend.getSlicersList().isEmpty())
        {
            // Set to zero so next wave can count correctly
            Slicer.setNumEnemies(0);
            this.isWave = false;
            close();
        }
    }

    // Get and set methods
    public TiledMap getMap() {
        return map;
    }

    public boolean getIsWave()
    {
        return isWave;
    }

    public Point getStart()
    {
        return start;
    }

    public List<Point> getPolyLines() {
        return polyLines;
    }
}
