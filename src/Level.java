import bagel.*;
import bagel.map.TiledMap;
import bagel.util.Colour;
import bagel.util.Point;
import static bagel.Window.close;
import static bagel.Window.getWidth;

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
    private static int lives = 25;


    // Variables
    private TiledMap map;
    private List<Point> polyLines;
    private Point start;

    private static int levelIndex = 0;
    private static ArrayList<Level> levelsList = new ArrayList<>();
    private static final String BUY_PANEL_PATH = "res/images/buypanel.png";
    private static final String STATUS_PANEL_PATH = "res/images/statuspanel.png";
    private static final String FONT_PATH = "res/fonts/DejaVuSans-Bold.ttf";
    private final static String AWAITING = "Awaiting Start";

    private static String status = AWAITING;

    static Image buyPanel;
    static Image statusPanel;

    // Constructor
    public Level(String levelPath)
    {
        this.map = new TiledMap(levelPath);
        polyLines = map.getAllPolylines().get(0);
        start = polyLines.get(0);
        buyPanel = new Image(BUY_PANEL_PATH);
        statusPanel = new Image(STATUS_PANEL_PATH);
    }


    // Loads all levels at once
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
    // Draws the panels for the game
    public static void drawPanels()
    {

        buyPanel.drawFromTopLeft(ORIGIN,ORIGIN);
        statusPanel.drawFromTopLeft(ORIGIN,HEIGHT - 2*statusPanel.getHeight());

        Font font = new Font(FONT_PATH, 18);

        // Dynamically updates content based on the status of the game
        String waveNum = "Wave: " + WaveManager.getCurrentWaveNum();
        if(WaveManager.getCurrentWaveNum() == 0)
        {
            waveNum = "Wave: ";
        }

        font.drawString(waveNum, ORIGIN + 5, HEIGHT - 35);

        String statusStr = "Status: " + status;
        font.drawString(statusStr, WIDTH/2-font.getWidth(statusStr)/2, HEIGHT - 35);

        DrawOptions drawOptions = new DrawOptions();
        if(ShadowDefend.getTimeScale() > 1)
        {
            drawOptions.setBlendColour(Colour.GREEN);
        }

        String TimeScaleStr = "Time Scale: " + ShadowDefend.getTimeScale();
        font.drawString(TimeScaleStr, WIDTH/4-font.getWidth("Time Scale: ")/2, HEIGHT - 35, drawOptions);

        String livesStr = "lives: " + lives;
        font.drawString(livesStr, WIDTH-2*font.getWidth(livesStr)/2 - 5, HEIGHT - 35, drawOptions);

    }

    public static void setStatus(String status) {
        Level.status = status;
    }

    public static String getAWAITING() {
        return AWAITING;
    }
}
