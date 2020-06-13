import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import static bagel.Window.close;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


// TODO: check spec, java docs

// TODO: every class: commenting, check modifiers, condensing if neccassary

/**
 * Main class of the shadowDefend game, it is responsible for maintaining lists of waveEvents, towers and enemies ect.
 * Also controls a most of the game logic inc tracking cash and lives.
 */
public class ShadowDefend extends AbstractGame {

    // int Constants
    private static final int WIDTH = 1024;
    private static final int INITIAL_LIVES = 25;
    private static final int HEIGHT = 768;
    private static final int ORIGIN = 0;
    private static final int SMALL_FONT = 18;
    private static final int LARGE_FONT = 48;
    private static final int PADDING = 5;
    private static final int MAX_TIMESCALE = 5;
    private static final int TANK_X = 64;
    private static final int SUPER_TANK_X = TANK_X + 120;
    private static final int AIR_SUPPORT_X = SUPER_TANK_X + 120;
    private static final int TOWER_Y = 40;
    private static final int INITIAL_CASH = 500;
    private static final int INITIAL_TIMESCALE = 1;
    private static final int EXTRA_PADDING = 10;
    private static final int PRICE_VER_OFFSET = 50;
    private static final int PRICE_HOR_OFFSET = 23;
    // Cash per wave function constants
    private static final int CASH_Y = 150;
    private static final int CASH_M = 100;
    private static final int CASH_STR_X_OFFSET = 50;
    private static final int CASH_STR_Y_OFFSET = 65;
    private static final int KEY_BINDS_Y_OFFSET = 25;

    // String Constants
    private static final String GAME_NAME = "ShadowDefend";
    private static final String BUY_PANEL_PATH = "res/images/buypanel.png";
    private static final String STATUS_PANEL_PATH = "res/images/statuspanel.png";
    private static final String WAVE_PATH = "res/levels/waves.txt";
    private static final String FONT_PATH = "res/fonts/DejaVuSans-Bold.ttf";
    private static final String WINNER = "Winner!";
    private static final String PLACING = "Placing";
    private static final String WAVE_IN_PROG = "Wave In Progress";
    private static final String AWAITING = "Awaiting Start";
    private static final String LOSER = "You lost!";
    private static final String LEVEL_PASSSED = "You have completed the level!";
    private static final String CONTINUE = "Click to continue.";
    private static final String CLOSE_WINDOW = "Press any key to close the window.";
    private static final String SPAWN = "spawn";
    private static final String DELAY = "delay";

    // ints
    private int lives = INITIAL_LIVES;
    private int levelIndex = 0;
    private int cash = INITIAL_CASH;
    private int waveNum = 1;
    private int waveEventIndex = 0;

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
    private final List<Level> levelList = new ArrayList<>();
    private final List<Projectile> projectileList = new ArrayList<>();
    private final List<Slicer> slicerList = new ArrayList<>();
    private final ArrayList<Tower> uniqueTowers = new ArrayList<>();
    private final List<Tower> towerList = new ArrayList<>();
    private final ArrayList<WaveEvent> waveEvents = new ArrayList<>();


    // Custom classes
    private static Input userInput;
    private final Tank tank;
    private final SuperTank superTank;
    private final AirSupport airSupport;
    private static Image statusPanel, buyPanel;


    // Constructor
    private ShadowDefend() {
        // Create the abstract game
        super(WIDTH, HEIGHT, GAME_NAME);

        // Read in the levels supplied
        Level.loadLevels(levelList);

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
        // Get the waveEvents
        readInWaves();

    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        // Constructs and runs the game
        ShadowDefend shadowDefend = new ShadowDefend();
        // Runs the game
        shadowDefend.run();
    }

    /**
     * @param input: is the user input each update.
     */
    // Called 60 times per second to update the game
    @Override
    public void update(Input input) {

        // Calculates the time scale
        calcTimeScale(input);

        userInput = input;
        // Draws the current levels map
        levelList.get(levelIndex).draw();

        // Out of lives
        if (lives <= 0) {
            gameLost(input);
            return;
        }
        // Level is passed but game not won
        if (levelComplete && !isWinner) {
            levelPassed(input);
            return;
        }
        // Level is passed and game is won
        if (isWinner) {
            drawStackedStrings(WINNER, CLOSE_WINDOW);

            if (input.wasPressed(MouseButtons.LEFT)) {
                close();
            }
            return;
        }
        // Checks if the user bought a tower and
        checkBought(input);

        // Updates the towers
        for (Tower tower : towerList) {
            tower.update(slicerList, this);
        }
        // Removes the airsupport if its out of the game
        towerList.removeIf(tower -> {
            if (isAirSupport(tower)) {
                AirSupport _airSupport = (AirSupport) tower;
                return _airSupport.isBombsEmpty() && !inPlay(tower.getPos(), EXTRA_PADDING);
            }
            return false;
        });
        // Begins the wave
        beginWave(input);
        // Updates the Slicers
        Slicer.update(this);

        // Removes projectiles if they hit
        projectileList.removeIf(projectile -> !projectile.update(timeScale, this));

        // Updates the individual wave event if in progress
        if (getCurrentWaveEvent().getInProgress()) {
            isWaveInProg = true;
            updateWaveEvent(timeScale);
        }
        // If the wave is over and all the slicers are gone and there are more waves to come then go to next wave
        if (endOfWave && slicerList.isEmpty() && !(waveEventIndex == waveEvents.size() - 1)) {
            nextWave();
        }
        // Draw all the panels at the end
        drawPanels();
    }

    // Action to be done if a level is passed
    private void levelPassed(Input input)
    {
        // Cleans the map
        deleteTowers();

        drawStackedStrings(LEVEL_PASSSED, CONTINUE);
        // Left click to continue
        if (input.wasPressed(MouseButtons.LEFT)) {
            // Updated status
            isAwaiting = true;
            isPlacing = false;
            isWaveInProg = false;
            lives = INITIAL_LIVES;

            if (levelIndex < levelList.size() - 1) {
                this.levelIndex++;
            } else {
                // Winner winner chicken dinner
                isWinner = true;
            }
            deleteTowers();

            levelComplete = false;
            // Resets cash
            cash = INITIAL_CASH;
            AirSupport.resetIsHorizontal();
        }

    }


    // Is called when plays lose all of there lives
    private void gameLost(Input input)
    {
        drawStackedStrings(LOSER, CLOSE_WINDOW);

        if (input.wasPressed(MouseButtons.LEFT)) {
            close();
        }
    }

    // Time scale is changed by the user and affects the speed of the entire game
    private void calcTimeScale(Input input) {
        // Uses keyboard input to determine speed of the game
        if (input.wasPressed(Keys.L) && timeScale < MAX_TIMESCALE) {
            timeScale++;
        }
        if (input.wasPressed(Keys.K) && timeScale > 1) {
            timeScale--;
        }
    }


    /**
     * @param cash is the amount of cash to add to the players total
     */
    // Allows manipulation of the cash from outside shadow defend
    public void addCash(int cash) {
        this.cash = this.cash + cash;
    }

    /**
     * @param cash is the cash to minus from the players total cash
     */
    public void minusCash(int cash) {
        this.cash = this.cash - cash;
    }

    // Determines the priority order of status
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


    /**
     * @param slicer is the slicer to be added to the games slicer list
     */
    // Add a slicer to the slicer list can be used from outside shadow defend class
    public void addSlicer(Slicer slicer) {
        slicerList.add(slicer);
    }

    /*
     * Tower functions
     ******************************************************************************************************************/
    private boolean isAirSupport(Tower tower) {
        return tower instanceof AirSupport;
    }

    /**
     * @param pos is the position to check if in play.
     * @param offset is an offset that can be applied to extend the playing area providing padding.
     * @return a boolean determining if the pos is in play or not.
     */
    // Checks if point is in play, an offset can be applied to give extra padding around the area to check
    public boolean inPlay(Point pos, int offset) {

        return !(pos.x < ORIGIN - offset) && !(pos.y < ORIGIN - offset)
                && !(pos.x > WIDTH + offset) && !(pos.y > HEIGHT + offset);
    }
    // Logic for buying towers
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
            // Check if already placing tower
        } else if (isPlacing) {
            // Discards the selection
            if (input.wasPressed(MouseButtons.RIGHT)) {
                isPlacing = false;
                for (Tower tower : uniqueTowers) {
                    if (tower.getIsBuying()) {
                        tower.setIsBuying(false);
                    }
                }
            }

            for (Tower tower : uniqueTowers) {
                // if possible to place then place
                if (isPlaceable(tower, input)) {
                    tower.draw((int) input.getMouseX(), (int) input.getMouseY());
                }
                // If possible to place and left clicked then placed tower
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

    // Checks if location is valid
    private boolean isPlaceable(Tower tower, Input input) {
        if (input.getMouseX() <= ORIGIN || input.getMouseX() >= WIDTH || input.getMouseY() <= ORIGIN || input.getMouseY() >= HEIGHT) {
            return false;
        }
        return tower.getIsBuying() &&
                !levelList.get(levelIndex).getMap().getPropertyBoolean((int) input.getMouseX(), (int) input.getMouseY(), "blocked", false)
                && !intersectsTowers(input.getMousePosition())
                && !statusPanel.getBoundingBoxAt(new Point((double) WIDTH / 2, HEIGHT - statusPanel.getHeight() / 2)).intersects(input.getMousePosition())
                && !buyPanel.getBoundingBoxAt(new Point((double) WIDTH / 2, ORIGIN + buyPanel.getHeight() / 2)).intersects(input.getMousePosition());
    }

    // Checks if a position intersects with towers
    private boolean intersectsTowers(Point pos) {
        for (Tower tower : towerList) {
            if (tower.getBoundingBox().intersects(pos)) {
                return true;
            }
        }
        return false;
    }

    // Clears the tower list
    private void deleteTowers() {
        towerList.clear();
    }

    /*
     * Waves Managing
     ******************************************************************************************************************/
    private void updateWaveEvent(double timeScale) {
        // Ensures after all waves are finished we don't try to update
        if (!(waveEventIndex == waveEvents.size() - 1)) {
            WaveEvent wave = waveEvents.get(waveEventIndex);
            // If its a swan event
            if (wave.getAction().equals(SPAWN)) {
                // if its time to spawn a new slicer
                if (wave.checkTimer() >= wave.getSpawnDelay() / timeScale && wave.getNumSpawned() < wave.getNumToSpawn()) {
                    wave.resetTimer();
                    wave.spawnSlicer(this);
                }
                // if all the slicers have been spawned
                if (wave.getNumSpawned() >= wave.getNumToSpawn()) {
                    endWaveEvent(this);
                }
            }
            // Delay event action
            if (wave.getAction().equals(DELAY)) {
                if (wave.checkTimer() >= wave.getDelay() / timeScale) {
                    endWaveEvent(this);
                }
            }
        // If all the slicers are
        } else if (slicerList.isEmpty()) {

            waveEventIndex = 0;
            waveNum = 1;
            endOfWave = false;
            levelComplete = true;

            for (WaveEvent wave : waveEvents) {
                wave.setNumSpawned(0);
            }
        }
    }

    // Reads in waves from levels sub folder
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

    private void nextWave() {

        isWaveInProg = false;
        isAwaiting = true;

        addCash(CASH_Y + CASH_M * waveNum);
        this.waveNum++;
        endOfWave = false;
    }

    // The current wave event
    private WaveEvent getCurrentWaveEvent() {
        return waveEvents.get(waveEventIndex);
    }

    // begins a wave
    private void beginWave(Input input) {
        if (input.wasPressed(Keys.S) && !getCurrentWaveEvent().getInProgress() && slicerList.isEmpty()) {
            isWaveInProg = true;
            beginWaveEvent(this);
        }

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
        // If wave event is part of the same wave number begin immediately
        if (waveEvents.get(waveEventIndex).getWaveNumber() == waveEvents.get(waveEventIndex - 1).getWaveNumber()) {
            beginWaveEvent(shadowDefend);

        } else {

            // Ends a wave
            endOfWave = true;
        }
    }


    /*
     * Drawing
     ******************************************************************************************************************/
    // Draws the panels for the game
    private void drawPanels() {

        buyPanel.drawFromTopLeft(ORIGIN, ORIGIN);
        Font font = new Font(FONT_PATH, SMALL_FONT);

        drawStatusPanel(font, statusPanel);

        drawTowers(font);

        // Draw Cash with commas
        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);

        String cashStr = "$" + myFormat.format(cash);
        font = new Font(FONT_PATH, LARGE_FONT);
        font.drawString(cashStr, WIDTH - font.getWidth(cashStr) - CASH_STR_X_OFFSET, CASH_STR_Y_OFFSET);


    }
    // Implemented like this for centering purposes
    private void drawStackedStrings(String string1, String string2) {
        Font font = new Font(FONT_PATH, LARGE_FONT);
        font.drawString(string1, (double) WIDTH / 2 - font.getWidth(string1) / 2, (double) HEIGHT / 2);
        font.drawString(string2, (double) WIDTH / 2 - font.getWidth(string2) / 2, (double) HEIGHT / 2 + LARGE_FONT);
    }

    // Draws everything required for the status panel
    private void drawStatusPanel(Font font, Image statusPanel) {
        statusPanel.drawFromTopLeft(ORIGIN, HEIGHT - statusPanel.getHeight());
        // Wave number
        // Dynamically updates content based on the status of the game

        // Draws wave number
        String waveNum = "Wave: " + this.waveNum;
        font.drawString(waveNum, ORIGIN + PADDING, HEIGHT - PADDING);

        // Current status
        setStatus();
        String statusStr = "Status: " + status;
        font.drawString(statusStr, (double) WIDTH / 2 - font.getWidth(statusStr) / 2, HEIGHT - PADDING);

        // Draws the time scale
        DrawOptions drawOptions = new DrawOptions();
        if (ShadowDefend.getTimeScale() > 1) {
            drawOptions.setBlendColour(Colour.GREEN);
        }

        String TimeScaleStr = "Time Scale: " + ShadowDefend.getTimeScale();
        font.drawString(TimeScaleStr, (double) WIDTH / 4 - font.getWidth("Time Scale: ") / 2, HEIGHT - PADDING, drawOptions);

        // Draws the lives
        String livesStr = "lives: " + lives;
        font.drawString(livesStr, WIDTH - 2 * font.getWidth(livesStr) / 2 - PADDING, HEIGHT - PADDING, drawOptions);

        // Key bind instructions
        String keyBinds = "Key binds:\nS - Start Wave\nL - Increase Timescale\nK - Decrease Timescale";
        font.drawString(keyBinds, (WIDTH - font.getWidth(keyBinds)) / 2, KEY_BINDS_Y_OFFSET);
    }

    // Draws the towers for the buy panel
    private void drawTowers(Font font) {

        DrawOptions drawOptions;
        drawOptions = setColour(tank);

        tank.draw();
        String tankPrice = "$" + tank.getPrice();
        font.drawString(tankPrice, TANK_X - PRICE_HOR_OFFSET, TOWER_Y + PRICE_VER_OFFSET, drawOptions);

        drawOptions = setColour(superTank);
        superTank.draw();
        String superTankPrice = "$" + superTank.getPrice();
        font.drawString(superTankPrice, SUPER_TANK_X - PRICE_HOR_OFFSET, TOWER_Y + PRICE_VER_OFFSET, drawOptions);

        drawOptions = setColour(airSupport);
        airSupport.draw(AIR_SUPPORT_X, TOWER_Y);
        String airSupportPrice = "$" + airSupport.getPrice();
        font.drawString(airSupportPrice, AIR_SUPPORT_X - PRICE_HOR_OFFSET, TOWER_Y + PRICE_VER_OFFSET, drawOptions);

    }

    // Helps user know if they have enough cash to buy tower
    private DrawOptions setColour(Tower tower) {
        DrawOptions drawOptions = new DrawOptions();

        if (tower.getPrice() <= cash) {
            drawOptions.setBlendColour(Colour.GREEN);
        } else {
            drawOptions.setBlendColour(Colour.RED);
        }

        return drawOptions;
    }

    /*
     *Getters and setters
     ******************************************************************************************************************/

    /**
     * @return returns the games current timeScale multiplier
     */
    public static Input getUserInput() {
        return userInput;
    }

    /**
     * @return The games current timeScale multiplier.
     */
    public static double getTimeScale() {
        return timeScale;
    }

    /**
     * @return The current Level of the game.
     */
    public Level getCurrentLevel() {
        return levelList.get(levelIndex);
    }

    /**
     * @param lives The players current lives.
     */
    public void setLives(int lives) {
        this.lives = lives;
    }

    /**
     * @return The current players lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * @return The list of ground tower projectiles.
     */
    public List<Projectile> getProjectileList() {
        return projectileList;
    }

    /**
     * @return The list of slicers in the game.
     */
    public List<Slicer> getSlicerList() {
        return slicerList;
    }

    /**
     * @return The string "delay" found in waves.txt used to determine the type of waveEvent.
     */
    // Using this to avoid having to copy's of these static
    public static String getDELAY() {
        return DELAY;
    }

    /**
     * @return The string "spawn" found in waves.txt used to determine the type of waveEvent.
     */
    public static String getSPAWN() {
        return SPAWN;
    }

    /**
     * @return The height of the game panel.
     */
    public static int getHEIGHT() {
        return HEIGHT;
    }

    /**
     * @return The width of the game panel.
     */
    public static int getWIDTH() {
        return WIDTH;
    }

    /**
     * @return The origin of the game window.
     */
    public static int getORIGIN() {
        return ORIGIN;
    }


}