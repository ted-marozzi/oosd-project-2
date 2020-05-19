
import bagel.map.TiledMap;

import bagel.util.Point;

import java.util.List;

// Class which holds information specific to a level/map
public class Level
{

    // Variables
    private TiledMap map;
    private List<Point> polyLines;
    private Point start;

    // Constructor
    public Level(String levelPath)
    {
        this.map = new TiledMap(levelPath);
        polyLines = map.getAllPolylines().get(0);
        start = polyLines.get(0);


    }



    public Point getStart()
    {
        return start;
    }

    public List<Point> getPolyLines() {
        return polyLines;
    }



    public void draw() {
        int ORIGIN = ShadowDefend.getORIGIN();
        map.draw(ORIGIN,ORIGIN,ORIGIN,ORIGIN,ShadowDefend.getWIDTH(), ShadowDefend.getHEIGHT());

    }


}
