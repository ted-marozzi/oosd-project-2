// Bagel Imports

import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static bagel.Window.close;
// Java Imports


public class ShadowDefend extends AbstractGame {

    private final ArrayList<WaveEvent> waveEvents = new ArrayList<>();
    private int waveEventIndex = 0;
    private static final String SPAWN = "spawn";
    private static final String DELAY = "delay";
    private static final String WAVE_PATH = "res/levels/waves.txt";

    private int currentWaveNum = 1;


    // int Constants
    private static final int WIDTH = 1024;
    private static final int INITIAL_LIVES = 25;
    private static final int HEIGHT = 768;
    private static final int ORIGIN = 0;
    private static final int SMALL_FONT = 18;
    private static final int LARGE_FONT = 60;
    private static final int PADDING = 5;
    private static final int MAX_TIMESCALE = 5;
    private static final int TANK_X = 64;
    private static final int SUPER_TANK_X = TANK_X + 120;
    private static final int AIR_SUPPORT_X = SUPER_TANK_X + 120;
    private static final int TOWER_Y = 40;
    private static final int INITAL_CASH = 500;
    private static final int INITIAL_TIMESCALE = 1;

    // String Constants
    private static final String GAME_NAME = "ShadowDefend";
    private static final String LOSER = "You lost!";
    private static final String LEVEL_PASSSED = "You have completed the level!";
    private static final String CONTINUE = "Click to continue.";
    private static final String CLOSE_WINDOW = "Press any key to close the window.";
    private static final String BUY_PANEL_PATH = "res/images/buypanel.png";
    private static final String STATUS_PANEL_PATH = "res/images/statuspanel.png";
    private static final String FONT_PATH = "res/fonts/DejaVuSans-Bold.ttf";
    private static final String WINNER = "Winner!";
    private static final String PLACING = "Placing";
    private static final String WAVE_IN_PROG = "Wave In Progress";
    private static final String AWAITING = "Awaiting Start";

    // ints
    private int lives = INITIAL_LIVES;
    private int levelIndex = 0;
    private int cash = INITAL_CASH;

    // doubles
    private static double timeScale = INITIAL_TIMESCALE;

    // Strings
    private String status;

    // booleans
    private boolean isWinner = false;
    private boolean isPlacing = false;
    private boolean isWaveInProg = false;
    private boolean isAwaiting = true;
    private boolean levelComplete = false;
    private boolean endOfWave = false;

    // lists
    private final List<Level> levelsList = new ArrayList<>();
    private final List<Projectile> projectileList = new ArrayList<>();
    private final List<Slicer> slicerList = new ArrayList<>();
    private final ArrayList<Tower> uniqueTowers = new ArrayList<>();
    private final List<Tower> towerList = new ArrayList<>();

    // Custom classes
    private static Input userInput;
    private final Tank tank;
    private final SuperTank superTank;
    private final AirSupport airSupport;
    private static ShadowDefend shadowDefend;
    private static Image statusPanel, buyPanel;


    // Constructor
    private ShadowDefend() {
        // Create the abstract game
        super(WIDTH, HEIGHT, GAME_NAME);

        // Read in the levels supplied
        loadLevels();

        // Create panel image objects
        statusPanel = new Image(STATUS_PANEL_PATH);
        buyPanel = new Image(BUY_PANEL_PATH);

        // Create tower objects for the panels
        tank = new Tank(new Point(TANK_X, TOWER_Y));
        superTank = new SuperTank(new Point(SUPER_TANK_X, TOWER_Y));
        airSupport = new AirSupport(new Point(AIR_SUPPORT_X, TOWER_Y));
        // Add them
        uniqueTowers.add(tank);
        uniqueTowers.add(superTank);
        uniqueTowers.add(airSupport);

        readInWaves();

    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        shadowDefend = new ShadowDefend();
        // Runs the game
        shadowDefend.run();
    }

    @Override
    public void update(Input input) {
        Font font = new Font(FONT_PATH, LARGE_FONT);
        // Calculates the time scale
        calcTimeScale(input);
        // Sets the status panel
        setStatus();

        userInput = input;
        levelsList.get(levelIndex).draw();


        if (lives <= 0) {

            font.drawString(LOSER, WIDTH / 2 - font.getWidth(WINNER) / 2, HEIGHT / 2);
            font.drawString(CLOSE_WINDOW, WIDTH / 2 - font.getWidth(CLOSE_WINDOW) / 2, HEIGHT / 2 + LARGE_FONT);

            if (input.wasPressed(MouseButtons.LEFT)) {

                close();
            }
            return;

        }

        if(levelComplete && !isWinner)
        {
            deleteTowers();

            font.drawString(LEVEL_PASSSED, WIDTH / 2 - font.getWidth(LEVEL_PASSSED) / 2, HEIGHT / 2);
            font.drawString(CONTINUE, WIDTH / 2 - font.getWidth(CONTINUE) / 2, HEIGHT / 2 + LARGE_FONT);

            if (input.wasPressed(MouseButtons.LEFT)) {


                isAwaiting = true;
                isPlacing = false;
                isWaveInProg = false;
                lives = INITIAL_LIVES;
                nextLevel();
                deleteTowers();

                levelComplete = false;
                resetCash();
            }

        }

        if (isWinner) {

            font.drawString(WINNER, WIDTH / 2 - font.getWidth(WINNER) / 2, HEIGHT / 2);
            font.drawString(CLOSE_WINDOW, WIDTH / 2 - font.getWidth(CLOSE_WINDOW) / 2, HEIGHT / 2 + LARGE_FONT);

        }

        if (input.wasPressed(MouseButtons.LEFT) && isWinner) {

            close();
        }


        // Draws the current level


        checkBought(input);

        for (Tower tower : towerList) {
            tower.update(slicerList, shadowDefend);

        }

        towerList.removeIf(tower -> {
            if (isAirSupport(tower)) {
                AirSupport _airSupport = (AirSupport) tower;
                return _airSupport.isBombsEmpty() && !inPlay(tower.getPos(), 11);
            }
            return false;
        });


        beginWave(input, shadowDefend);

        Slicer.update(shadowDefend);
        shadowDefend.getProjectileList().removeIf(projectile -> !projectile.update(timeScale, this));

        if (getCurrentWaveEvent().getInProgress()) {// TODO: remove magic nums and strings
            isWaveInProg = true;
            updateWaveEvent(timeScale, shadowDefend);
        }


        if (endOfWave && slicerList.isEmpty() && !(waveEventIndex == waveEvents.size() - 1)) {

            isWaveInProg = false;
            isAwaiting = true;

            addCash(150 + 100 * currentWaveNum);
            nextWave();

            endOfWave = false;
        }


        drawPanels();



    }

    private boolean isAirSupport(Tower tower) {
        return tower instanceof AirSupport;
    }

    // Checks if point is in play, an offset can be applied to give extra padding around the area to check
    public boolean inPlay(Point pos, int offset) {

        if (pos.x < ORIGIN - offset || pos.y < ORIGIN - offset || pos.x > WIDTH + offset || pos.y > HEIGHT + offset) {
            return false;
        }
        return true;
    }

    private void checkBought(Input input) {
        // Check if item bought?
        if (input.wasPressed(MouseButtons.LEFT) && !isPlacing) {
            for (Tower tower : uniqueTowers) {
                if (tower.wasClicked(input) && cash >= tower.getPrice()) {

                    tower.setIsBuying(true);
                    isPlacing = true;
                    break;
                }
            }

        } else if (isPlacing) {

            if (input.wasPressed(MouseButtons.RIGHT)) {
                isPlacing = false;
                for (Tower tower : uniqueTowers) {
                    if (tower.getIsBuying()) {
                        tower.setIsBuying(false);


                    }

                }


            }

            for (Tower tower : uniqueTowers) {
                if (isPlaceable(tower, input)) {
                    tower.draw((int) input.getMouseX(), (int) input.getMouseY());
                }

                if (isPlaceable(tower, input) && input.wasPressed(MouseButtons.LEFT)) {
                    if (tower.getIsBuying()) {
                        minusCash(tower.getPrice());
                        isPlacing = false;
                        tower.setIsBuying(false);
                        Tower _tower = tower.create(new Point(input.getMouseX(), input.getMouseY()));
                        towerList.add(_tower);
                        tower.draw();

                    }

                }


            }

        }

    }



    private boolean isPlaceable(Tower tower, Input input) {
        if (input.getMouseX() <= ORIGIN || input.getMouseX() >= WIDTH || input.getMouseY() <= ORIGIN || input.getMouseY() >= HEIGHT) {
            return false;
        }
        return tower.getIsBuying() &&
                !levelsList.get(levelIndex).getMap().getPropertyBoolean((int) input.getMouseX(), (int) input.getMouseY(), "blocked", false)
                && !interectsTowers(input) && !statusPanel.getBoundingBoxAt(new Point(WIDTH / 2, HEIGHT - statusPanel.getHeight() / 2)).intersects(input.getMousePosition())
                && !buyPanel.getBoundingBoxAt(new Point(WIDTH / 2, ORIGIN + buyPanel.getHeight() / 2)).intersects(input.getMousePosition());
    }

    private boolean interectsTowers(Input input) {

        for (Tower tower : towerList) {
            //if(input.getMouseX() >= tower.getBoundingBox().left() && input.getMouseX() <= tower.getBoundingBox().right()
            //&& input.getMouseY() >= tower.getBoundingBox().top() && input.getMouseY() <= tower.getBoundingBox().bottom())
            if (tower.getBoundingBox().intersects(input.getMousePosition())) {
                return true;
            }
        }
        return false;

    }


    public void deleteTowers() {
        towerList.clear();
    }


    private void calcTimeScale(Input input) {
        // Uses keyboard input to determine speed of the game
        if (input.wasPressed(Keys.L) && timeScale < MAX_TIMESCALE)
            timeScale++;
        if (input.wasPressed(Keys.K) && timeScale > 1)
            timeScale--;

    }

    // Loads all levels at once
    private void loadLevels() {
        levelIndex = 1;
        while (true) {
            String path = "res/levels/" + levelIndex + ".tmx";
            if (Files.exists(Paths.get(path))) {
                levelsList.add(new Level(path));
            } else
                break;
            levelIndex++;
        }
        levelIndex = 0;
    }

    // Draws the panels for the game
    private void drawPanels() {

        buyPanel.drawFromTopLeft(ORIGIN, ORIGIN);


        Font font = new Font(FONT_PATH, SMALL_FONT);

        drawStatusPanel(font, statusPanel);

        drawTowers(font);

        // Draw Cash
        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);

        String cashStr = "$" + myFormat.format(cash);
        font = new Font(FONT_PATH, LARGE_FONT);
        font.drawString(cashStr, WIDTH - font.getWidth(cashStr) - 50, 65);


    }

    private void drawStatusPanel(Font font, Image statusPanel) {
        statusPanel.drawFromTopLeft(ORIGIN, HEIGHT - statusPanel.getHeight());

        // Dynamically updates content based on the status of the game
        String waveNum = "Wave: " + currentWaveNum;



        font.drawString(waveNum, ORIGIN + PADDING, HEIGHT - PADDING);

        String statusStr = "Status: " + status;
        font.drawString(statusStr, WIDTH / 2 - font.getWidth(statusStr) / 2, HEIGHT - PADDING);

        DrawOptions drawOptions = new DrawOptions();
        if (ShadowDefend.getTimeScale() > 1) {
            drawOptions.setBlendColour(Colour.GREEN);
        }

        String TimeScaleStr = "Time Scale: " + ShadowDefend.getTimeScale();
        font.drawString(TimeScaleStr, WIDTH / 4 - font.getWidth("Time Scale: ") / 2, HEIGHT - PADDING, drawOptions);

        String livesStr = "lives: " + lives;
        font.drawString(livesStr, WIDTH - 2 * font.getWidth(livesStr) / 2 - PADDING, HEIGHT - PADDING, drawOptions);


        String keyBinds = "Key binds:\nS - Start Wave\nL - Increase Timescale\nK - Decrease Timescale";
        font.drawString(keyBinds, (WIDTH - font.getWidth(keyBinds)) / 2, 25);
    }

    private void drawTowers(Font font) {

        int priceVerOffset = 50;
        int priceHorOffset = 23;


        DrawOptions drawOptions;
        drawOptions = setColour(tank);

        tank.draw();
        String tankPrice = "$" + tank.getPrice();
        font.drawString(tankPrice, TANK_X - priceHorOffset, TOWER_Y + priceVerOffset, drawOptions);

        drawOptions = setColour(superTank);
        superTank.draw();
        String superTankPrice = "$" + superTank.getPrice();
        font.drawString(superTankPrice, SUPER_TANK_X - priceHorOffset, TOWER_Y + priceVerOffset, drawOptions);

        drawOptions = setColour(airSupport);
        airSupport.draw(AIR_SUPPORT_X, TOWER_Y);
        String airSupportPrice = "$" + airSupport.getPrice();
        font.drawString(airSupportPrice, AIR_SUPPORT_X - priceHorOffset, TOWER_Y + priceVerOffset, drawOptions);

    }

    private DrawOptions setColour(Tower tower) {
        DrawOptions drawOptions = new DrawOptions();

        if (tower.getPrice() <= cash) {
            drawOptions.setBlendColour(Colour.GREEN);
        } else {
            drawOptions.setBlendColour(Colour.RED);
        }

        return drawOptions;
    }

    public void addCash(int cash) {
        this.cash = this.cash + cash;
    }

    public void minusCash(int cash) {
        this.cash = this.cash - cash;
    }


    public void addSlicer(Slicer slicer) {
        slicerList.add(slicer);
    }



    private void setStatus() {
        if (isWinner) {
            this.status = WINNER;
        } else if (isPlacing) {
            this.status = PLACING;
        } else if (isWaveInProg) {
            this.status = WAVE_IN_PROG;

        } else if (isAwaiting) {
            this.status = AWAITING;
        }

    }




    private void nextLevel() {


        if (levelIndex < levelsList.size() - 1) {
            this.levelIndex++;
        } else {

            isWinner = true;
        }

    }


    private void readInWaves() {

        FileInputStream wavesStream = null;
        try {
            wavesStream = new FileInputStream(WAVE_PATH);

        } catch (IOException io) {
            io.printStackTrace();
        }

        assert wavesStream != null;
        Scanner sc = new Scanner(wavesStream);
        while (sc.hasNextLine()) {
            waveEvents.add(new WaveEvent(sc.nextLine()));
        }
    }

    private void nextWave()
    {
        this.currentWaveNum++;
    }

    // The current wave event
    private WaveEvent getCurrentWaveEvent() {
        return waveEvents.get(waveEventIndex);
    }

    // begins a single wave event
    private void beginWaveEvent(ShadowDefend shadowDefend) {

        WaveEvent wave = waveEvents.get(waveEventIndex);
        wave.setInProgress(true);
        wave.startTimer();


        if (wave.getAction().equals(SPAWN)) {
            wave.spawnSlicer(shadowDefend);
        }


    }

    // Ends a single wave event
    private void endWaveEvent(ShadowDefend shadowDefend) {

        WaveEvent wave = waveEvents.get(waveEventIndex);
        wave.setInProgress(false);
        waveEventIndex++;

        if (waveEvents.get(waveEventIndex).getWaveNumber() == waveEvents.get(waveEventIndex - 1).getWaveNumber()) {
            beginWaveEvent(shadowDefend);

        } else {
            endOfWave = true;

        }

    }


    private void updateWaveEvent(double timeScale, ShadowDefend shadowDefend) {
        // Ensures after all waves are finished we don't try to update
        if (!(waveEventIndex == waveEvents.size() - 1)) {
            WaveEvent wave = waveEvents.get(waveEventIndex);
            if (wave.getAction().equals(SPAWN)) {

                if (wave.checkTimer() >= wave.getSpawnDelay() / timeScale && wave.getNumSpawned() < wave.getNumToSpawn()) {
                    wave.resetTimer();
                    wave.spawnSlicer(shadowDefend);
                }

                if (wave.getNumSpawned() >= wave.getNumToSpawn()) {
                    endWaveEvent(shadowDefend);
                }


            }

            if (wave.getAction().equals(DELAY)) {
                if (wave.checkTimer() >= wave.getDelay() / timeScale) {
                    endWaveEvent(shadowDefend);

                }

            }

        } else if (shadowDefend.getSlicerList().isEmpty()) {

            waveEventIndex = 0;
            currentWaveNum = 1;
            endOfWave = false;
            levelComplete = true;

            for( WaveEvent wave: waveEvents)
            {
                wave.setNumSpawned(0);
            }
        }
    }

    // begins a wave
    private void beginWave(Input input, ShadowDefend shadowDefend) {
        if (input.wasPressed(Keys.S) && !getCurrentWaveEvent().getInProgress() && shadowDefend.getSlicerList().isEmpty()) {
            isWaveInProg = true;
            beginWaveEvent(shadowDefend);
        }

    }


    public void resetCash() {
        this.cash = INITAL_CASH;
    }


    public static Input getUserInput() {
        return userInput;
    }

    public static double getTimeScale() {
        return timeScale;
    }


    public List<Slicer> getSlicerList() {
        return slicerList;
    }

    public Level getCurrentLevel() {
        return levelsList.get(levelIndex);
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getORIGIN() {
        return ORIGIN;
    }
    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }

    public List<Projectile> getProjectileList() {
        return projectileList;
    }


    // Using this to avoid having to copy's of this static
    public static String getDELAY() {
        return DELAY;
    }
    public static String getSPAWN() {
        return SPAWN;
    }


}