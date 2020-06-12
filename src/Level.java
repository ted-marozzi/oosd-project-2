
import bagel.map.TiledMap;

import bagel.util.Point;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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



    public void draw() {
        int ORIGIN = ShadowDefend.getORIGIN();
        map.draw(ORIGIN,ORIGIN,ORIGIN,ORIGIN,ShadowDefend.getWIDTH(), ShadowDefend.getHEIGHT());

    }

    // Loads all levels at once
    public static void loadLevels(List<Level> levelList) {
        int levelIndex = 1;
        // Ensure the naming convention is correct when levels are added.
        while (true) {
            String path = "res/levels/" + levelIndex + ".tmx";
            if (Files.exists(Paths.get(path))) {
                levelList.add(new Level(path));
            } else
                break;
            levelIndex++;
        }

    }

    public TiledMap getMap() {
        return map;
    }

    public Point getStart()
    {
        return start;
    }

    public List<Point> getPolyLines() {
        return polyLines;
    }




}
