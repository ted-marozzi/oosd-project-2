// Bagel Imports
import bagel.*;
import bagel.util.Colour;

import java.awt.font.ImageGraphicAttribute;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
// Java Imports


// TODO ask if list should be static, if not how do I access them out side of Shadow defend, do i pass instance of game or do i make static

public class ShadowDefend extends AbstractGame {

    private static ShadowDefend _instance = null;
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static final int ORIGIN = 0;

    private static final String BUY_PANEL_PATH = "res/images/buypanel.png";
    private static final String STATUS_PANEL_PATH = "res/images/statuspanel.png";
    private static final String FONT_PATH = "res/fonts/DejaVuSans-Bold.ttf";
    private final static String AWAITING = "Awaiting Start";

    private String status = AWAITING;

    private int lives = 25;
    private int cash = 500;

    private int levelIndex = 0;
    private final List<Level> levelsList = new ArrayList<>();


    // Lists to keep track of how many levels and slicers there are
    private List<Slicer> slicerList = new ArrayList<>();


    // Timing
    private static final int INITIAL_TIMESCALE = 1;
    private static double timeScale = INITIAL_TIMESCALE;




    private WaveManager waveManager;

    public static ShadowDefend getInstance() {
        if(_instance == null)
            return _instance = new ShadowDefend();
        else
            return _instance;
    }

    // Constructor
    public ShadowDefend() {
        super(WIDTH,HEIGHT,"ShadowDefend");
        loadLevels();
        waveManager = WaveManager.getInstance();

    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        _instance = new ShadowDefend();
        _instance.run();

    }

    @Override
    public void update(Input input) {
        // Calculates the time scale
        calcTimeScale(input);
        // Draws the current level
        levelsList.get(levelIndex).draw();

        waveManager.beginWave(input);

        if(waveManager.getCurrentWaveEvent().getInProgress())
            waveManager.updateWaveEvent(timeScale);
        Slicer.update();
        if(waveManager.getEndOfWave() && slicerList.isEmpty())
            status = AWAITING;

        drawPanels();

    }

    private void calcTimeScale(Input input)
    {
        // Uses keyboard input to determine speed of the game
        if(input.wasPressed(Keys.L))
            timeScale ++;
        if(input.wasPressed(Keys.K) && timeScale > 1)
            timeScale --;

    }

    public void addSlicer(Slicer slicer)
    {
        slicerList.add(slicer);
    }

    public static double getTimeScale()
    {
        return timeScale;
    }

    // Loads all levels at once
    public void loadLevels()
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

    public List<Slicer> getSlicerList() {
        return slicerList;
    }

    public Level getCurrentLevel()
    {
        return levelsList.get(levelIndex);
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }
    public static int getWIDTH()
    {
        return WIDTH;
    }

    public static int getORIGIN() {
        return ORIGIN;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }


    // Draws the panels for the game
    public void drawPanels()
    {
        Image statusPanel = new Image(STATUS_PANEL_PATH);
        Image buyPanel = new Image(BUY_PANEL_PATH);
        //TODO: remove magic numbers
        int HEIGHT = ShadowDefend.getHEIGHT(), WIDTH = ShadowDefend.getWIDTH();

        buyPanel.drawFromTopLeft(ORIGIN,ORIGIN);
        statusPanel.drawFromTopLeft(ORIGIN,HEIGHT - 2*statusPanel.getHeight());

        Font font = new Font(FONT_PATH, 18);

        WaveManager waveManager = WaveManager.getInstance();

        // Dynamically updates content based on the status of the game
        String waveNum = "Wave: " + waveManager.getCurrentWaveNum();
        if(waveManager.getCurrentWaveNum() == 0)
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


        String keyBinds = "Key binds:\nS - Start Wave\nL - Increase Timescale\nK - Decrease Timescale";
        font.drawString(keyBinds, (WIDTH-font.getWidth(keyBinds))/2, 25);

        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);

        String cashStr = "$" + myFormat.format(cash);



        Tank.drawTank(64, 35);
        String tankPrice = "$" + Tank.getPrice();
        font.drawString(tankPrice, 64-23, 35+50);

        SuperTank.drawTank(64+120, 35);
        String superTankPrice = "$" + SuperTank.getPrice();
        font.drawString(superTankPrice, 64+120 - 23, 35+50);

        font = new Font(FONT_PATH, 60);
        font.drawString(cashStr, WIDTH-font.getWidth(cashStr) -  50, 65);



    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives()
    {
        return lives;
    }
}