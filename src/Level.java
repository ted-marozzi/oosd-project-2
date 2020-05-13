import bagel.Input;
import bagel.Keys;
import bagel.map.TiledMap;
import bagel.util.Point;
import static bagel.Window.close;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
// Class which holds information specific to a level/map
public class Level
{
    // Constants
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 800;
    private static final int ORIGIN = 0;


    // Variables
    private TiledMap map;
    private List<Point> polyLines;
    private Point start;

    private static int levelIndex = 0;
    private static ArrayList<Level> levelsList = new ArrayList<>();

    // Constructor
    public Level(String levelPath)
    {
        this.map = new TiledMap(levelPath);
        polyLines = map.getAllPolylines().get(0);
        start = polyLines.get(0);
    }



    public static void loadLevels()
    {
        levelIndex = 1;
        while(true)
        {
            String path = "res/levels/" + levelIndex + ".tmx";
            if (Files.exists(Paths.get(path)))
            {
                levelsList.add(new Level(path));
            }
            else
                break;
            levelIndex++;
        }
        levelIndex = 0;
    }

    // Get and set methods
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

    public static int getLevelIndex() {
        return levelIndex;
    }

    public static void setCurrentLevel(int currentLevel) {
        Level.levelIndex = currentLevel;
    }

    public static ArrayList<Level> getLevelsList() {
        return levelsList;
    }

    public static Level getLevel(int levelIndex)
    {
        return levelsList.get(levelIndex);
    }
    public static Level getCurrentLevel()
    {
        return levelsList.get(Level.levelIndex);
    }


    public void draw() {
        map.draw(ORIGIN,ORIGIN,ORIGIN,ORIGIN,WIDTH,HEIGHT);
    }
}
