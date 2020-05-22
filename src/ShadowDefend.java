// Bagel Imports
import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
// Java Imports


public class ShadowDefend extends AbstractGame {

    // TODO: unsinglton
    //TODO: limit =time scale

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static final int ORIGIN = 0;
    private static final int SMALL_FONT = 18;
    private static final int LARGE_FONT = 60;
    private static final int PADDING = 5;
    private static final int TOWER_SPACING = 120;
    private static final int TOWER_HOR_OFFSET = 64;
    private static final int TOWER_VER_OFFSET = 10;

    private static final String BUY_PANEL_PATH = "res/images/buypanel.png";
    private static final String STATUS_PANEL_PATH = "res/images/statuspanel.png";
    private static final String FONT_PATH = "res/fonts/DejaVuSans-Bold.ttf";
    private static final String AWAITING = "Awaiting Start";

    private static boolean isBuying = false;

    private String status = AWAITING;

    private int lives = 25;

    private int cash = 500;

    private int levelIndex = 0;
    private final List<Level> levelsList = new ArrayList<>();



    // Lists to keep track of how many levels and slicers there are
    private final List<Slicer> slicerList = new ArrayList<>();


    // Timing
    private static final int INITIAL_TIMESCALE = 1;
    private static double timeScale = INITIAL_TIMESCALE;

    private final List<Tower> towerList = new ArrayList<>();




    private final Tank tank;
    private final SuperTank superTank;
    private final AirSupport airSupport;
    private final ArrayList<Tower> uniqueTowers =  new ArrayList<>();

    private static ShadowDefend shadowDefend;






    private final WaveManager waveManager;


    // Constructor
    private ShadowDefend() {
        super(WIDTH,HEIGHT,"ShadowDefend");
        loadLevels();
        waveManager = WaveManager.getInstance();

        tank = new Tank();
        superTank = new SuperTank();
        airSupport = new AirSupport();

        uniqueTowers.add(tank);
        uniqueTowers.add(superTank);
        uniqueTowers.add(airSupport);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        shadowDefend = new ShadowDefend();
        shadowDefend.run();
    }

    @Override
    public void update(Input input) {
        // Calculates the time scale
        calcTimeScale(input);

        // Draws the current level
        levelsList.get(levelIndex).draw();

        // Check if item bought?
        if(input.wasPressed(MouseButtons.LEFT) && !isBuying)
        {
            for(Tower tower: uniqueTowers)
            {
                if(tower.wasClicked(input) && cash >= tower.getPrice())
                {
                    minusCash(tower.getPrice());
                    tower.setIsBuying(true);
                    isBuying = true;
                    break;
                }
            }

        }
        else if (isBuying)
        {

            for(Tower tower: uniqueTowers)
            {
                if(tower.getIsBuying())
                {
                    tower.draw((int)input.getMouseX(), (int)input.getMouseY());
                }
            }

            if(input.wasPressed(MouseButtons.LEFT) &&
                    !levelsList.get(levelIndex).getMap().getPropertyBoolean((int)input.getMouseX(),
                    (int)input.getMouseY(), "blocked", false))
            {
                for(Tower tower: uniqueTowers)
                {
                    if(tower.getIsBuying())
                    {

                        isBuying = false;
                        tower.setIsBuying(false);
                        Tower _tower =  tower.create();
                        _tower.setPos(input.getMousePosition());

                        towerList.add(_tower);
                    }
                }

            }
        }


        for(Tower tower: towerList)
        {
            tower.draw();

            tower.update();

        }







        waveManager.beginWave(input, shadowDefend);

        if(waveManager.getCurrentWaveEvent().getInProgress())
            waveManager.updateWaveEvent(timeScale, shadowDefend);

        Slicer.update(shadowDefend);

        if(waveManager.getEndOfWave() && slicerList.isEmpty())
        {
            status = AWAITING;

            addCash(150 + 100*waveManager.getCurrentWaveNum());
            waveManager.setEndOfWave(false);
        }


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

    // Loads all levels at once
    private void loadLevels()
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

    // Draws the panels for the game
    private void drawPanels()
    {


        Image statusPanel = new Image(STATUS_PANEL_PATH);
        Image buyPanel = new Image(BUY_PANEL_PATH);

        int HEIGHT = ShadowDefend.getHEIGHT(), WIDTH = ShadowDefend.getWIDTH();

        buyPanel.drawFromTopLeft(ORIGIN,ORIGIN);


        Font font = new Font(FONT_PATH, SMALL_FONT);

        drawStatusPanel(font, statusPanel);



        int verTowerPlacement = (int) (buyPanel.getHeight()/2 - TOWER_VER_OFFSET);
        drawTowers(verTowerPlacement, font);

        // Draw Cash
        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);

        String cashStr = "$" + myFormat.format(cash);
        font = new Font(FONT_PATH, LARGE_FONT);
        font.drawString(cashStr, WIDTH-font.getWidth(cashStr) -  50, 65);



    }

    private void drawStatusPanel(Font font, Image statusPanel)
    {
        statusPanel.drawFromTopLeft(ORIGIN,HEIGHT - statusPanel.getHeight());
        WaveManager waveManager = WaveManager.getInstance();
        // Dynamically updates content based on the status of the game
        String waveNum = "Wave: " + waveManager.getCurrentWaveNum();

        if(waveManager.getCurrentWaveNum() == 0)
        {
            waveNum = "Wave: ";
        }

        font.drawString(waveNum, ORIGIN + PADDING, HEIGHT - PADDING);

        String statusStr = "Status: " + status;
        font.drawString(statusStr, WIDTH/2-font.getWidth(statusStr)/2, HEIGHT - PADDING);

        DrawOptions drawOptions = new DrawOptions();
        if(ShadowDefend.getTimeScale() > 1)
        {
            drawOptions.setBlendColour(Colour.GREEN);
        }

        String TimeScaleStr = "Time Scale: " + ShadowDefend.getTimeScale();
        font.drawString(TimeScaleStr, WIDTH/4-font.getWidth("Time Scale: ")/2, HEIGHT - PADDING, drawOptions);

        String livesStr = "lives: " + lives;
        font.drawString(livesStr, WIDTH-2*font.getWidth(livesStr)/2 - PADDING, HEIGHT - PADDING, drawOptions);


        String keyBinds = "Key binds:\nS - Start Wave\nL - Increase Timescale\nK - Decrease Timescale";
        font.drawString(keyBinds, (WIDTH-font.getWidth(keyBinds))/2, 25);
    }

    private void drawTowers(int verTowerPlacement, Font font)
    {

        int priceVerOffset = 50;
        int priceHorOffset = 23;

        tank.draw(TOWER_HOR_OFFSET, verTowerPlacement);
        String tankPrice = "$" + tank.getPrice();
        font.drawString(tankPrice, TOWER_HOR_OFFSET-priceHorOffset, verTowerPlacement+priceVerOffset);

        superTank.draw(TOWER_HOR_OFFSET + TOWER_SPACING, verTowerPlacement);
        String superTankPrice = "$" + superTank.getPrice();
        font.drawString(superTankPrice, TOWER_HOR_OFFSET + TOWER_SPACING - priceHorOffset,
                verTowerPlacement + priceVerOffset);

        airSupport.draw(TOWER_HOR_OFFSET + 2 * TOWER_SPACING, verTowerPlacement);
        String airSupportPrice = "$" + airSupport.getPrice();
        font.drawString(airSupportPrice, TOWER_HOR_OFFSET + 2 * TOWER_SPACING - priceHorOffset,
                verTowerPlacement + priceVerOffset);

    }

    public void addCash(int cash) {
        this.cash = this.cash + cash;
    }
    public void minusCash(int cash) {
        this.cash = this.cash - cash;
    }


    public void addSlicer(Slicer slicer)
    {
        slicerList.add(slicer);
    }
    public void addTower(Tower tower)
    {
        towerList.add(tower);
    }

    public static double getTimeScale()
    {
        return timeScale;
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




    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives()
    {
        return lives;
    }


}