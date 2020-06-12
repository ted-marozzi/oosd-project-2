// Bagel Imports

import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static bagel.Window.close;
// Java Imports


public class ShadowDefend extends AbstractGame {

    // TODO: unsinglton, check all modifers, and java doc
    //TODO: limit =time scale

    private static final int WIDTH = 1024;
    private static final int INITIAL_LIVES = 25;
    private static final int HEIGHT = 768;
    private static final int ORIGIN = 0;
    private static final int SMALL_FONT = 18;
    private static final int LARGE_FONT = 60;
    private static final int PADDING = 5;
    private static final int MAX_TIMESCALE = 5;
    private static final int TIMEOUT = 5000;
    private static int frameCount = 0;

    private static final int TANK_X = 64;
    private static final int SUPER_TANK_X = TANK_X + 120;
    private static final int AIR_SUPPORT_X = SUPER_TANK_X + 120;

    private static final int TOWER_Y = 40;
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
    private static final int INITAL_CASH = 500;
    private int cash = INITAL_CASH;


    private static boolean isWinner = false;
    private static boolean isPlacing = false;
    private static boolean isWaveInProg = false;
    private static boolean isAwaiting = true;


    private int lives = INITIAL_LIVES;
    private static Input userInput;


    private int levelIndex = 0;
    private final List<Level> levelsList = new ArrayList<>();
    private final List<Projectile> projectileList = new ArrayList<>();


    // Lists to keep track of how many levels and slicers there are
    private final List<Slicer> slicerList = new ArrayList<>();


    // Timing
    private static final int INITIAL_TIMESCALE = 1;
    private static double timeScale = INITIAL_TIMESCALE;

    private final List<Tower> towerList = new ArrayList<>();


    private final Tank tank;
    private final SuperTank superTank;
    private final AirSupport airSupport;
    private final ArrayList<Tower> uniqueTowers = new ArrayList<>();

    private static ShadowDefend shadowDefend;

    private final WaveManager waveManager;
    private String status;
    private static Image statusPanel, buyPanel;
    private boolean levelComplete = false;


    // Constructor
    private ShadowDefend() {
        super(WIDTH, HEIGHT, "ShadowDefend");
        loadLevels();
        waveManager = new WaveManager();

        tank = new Tank(new Point(TANK_X, TOWER_Y));
        superTank = new SuperTank(new Point(SUPER_TANK_X, TOWER_Y));
        airSupport = new AirSupport(new Point(AIR_SUPPORT_X, TOWER_Y));

        uniqueTowers.add(tank);
        uniqueTowers.add(superTank);
        uniqueTowers.add(airSupport);

        statusPanel = new Image(STATUS_PANEL_PATH);
        buyPanel = new Image(BUY_PANEL_PATH);
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
        setStatus();
        userInput = input;
        levelsList.get(levelIndex).draw();


        if (lives <= 0) {

            Font font = new Font(FONT_PATH, LARGE_FONT);
            font.drawString(LOSER, WIDTH / 2 - font.getWidth(WINNER) / 2, HEIGHT / 2);

            font = new Font(FONT_PATH, SMALL_FONT);
            font.drawString(CLOSE_WINDOW, WIDTH / 2 - font.getWidth(CLOSE_WINDOW) / 2, HEIGHT / 2 + LARGE_FONT);

            if (input.wasPressed(MouseButtons.LEFT)) {

                close();
            }
            return;

        }

        if(levelComplete)
        {
            deleteTowers();
            Font font = new Font(FONT_PATH, LARGE_FONT);
            font.drawString(LEVEL_PASSSED, WIDTH / 2 - font.getWidth(LEVEL_PASSSED) / 2, HEIGHT / 2);
            font.drawString(CONTINUE, WIDTH / 2 - font.getWidth(CONTINUE) / 2, HEIGHT / 2 + LARGE_FONT);

            if (input.wasPressed(MouseButtons.LEFT)) {


                isAwaiting = true;
                isPlacing = false;
                isWaveInProg = false;
                lives = INITIAL_LIVES;
                nextLevel(input);
                deleteTowers();

                levelComplete = false;
                resetCash();
            }

        }


        // Draws the current level


        checkBought(input);

        for (Tower tower : towerList) {
            tower.update(slicerList, shadowDefend);

        }

        towerList.removeIf(tower -> {
            if (isAirsupport(tower)) {
                AirSupport _airSupport = (AirSupport) tower;
                return _airSupport.isBombsEmpty() && !inPlay(tower.getPos(), 11);
            }
            return false;
        });


        waveManager.beginWave(input, shadowDefend);

        Slicer.update(shadowDefend);
        shadowDefend.getProjectileList().removeIf(projectile -> !projectile.update(timeScale, this));

        if (waveManager.getCurrentWaveEvent().getInProgress()) {// TODO: remove magic nums and strings
            isWaveInProg = true;
            waveManager.updateWaveEvent(timeScale, shadowDefend);
        }


        if (waveManager.getEndOfWave() && slicerList.isEmpty() && !(waveManager.getWaveEventIndex() == waveManager.getWaveEvents().size() - 1)) {

            isWaveInProg = false;
            isAwaiting = true;

            addCash(150 + 100 * waveManager.getCurrentWaveNum());
            waveManager.nextWave();

            waveManager.setEndOfWave(false);
        }


        drawPanels();
// TODO check this
        if (isWinner) {
            Font font = new Font(FONT_PATH, LARGE_FONT);
            font.drawString(WINNER, WIDTH / 2 - font.getWidth(WINNER) / 2, HEIGHT / 2);
            font = new Font(FONT_PATH, SMALL_FONT);
            font.drawString(CLOSE_WINDOW, WIDTH / 2 - font.getWidth(CLOSE_WINDOW) / 2, HEIGHT / 2 + LARGE_FONT);

        }

        if (input.wasPressed(MouseButtons.LEFT) && isWinner) {

            close();
        }


    }

    private boolean isAirsupport(Tower tower) {
        return tower instanceof AirSupport;

    }

    public boolean inPlay(Point pos) {

        if (pos.x < ORIGIN || pos.y < ORIGIN || pos.x > WIDTH || pos.y > HEIGHT) {
            return false;
        }
        return true;
    }

    public boolean inPlay(Point pos, int offset) {

        if (pos.x < ORIGIN - offset || pos.y < ORIGIN - offset || pos.x > WIDTH + offset || pos.y > HEIGHT + offset) {
            return false;
        }
        return true;
    }

    public void setLevelComplete(boolean levelComplete) {
        this.levelComplete = levelComplete;
    }

    public static void setIsWinner(boolean isWinner) {
        ShadowDefend.isWinner = isWinner;
    }

    public int getLevelIndex() {
        return levelIndex;
    }

    public int getLevelsListSize() {
        return levelsList.size();
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
            isPlacing = true;

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

        drawStatusPanel(font, statusPanel, waveManager);

        drawTowers(font);

        // Draw Cash
        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);

        String cashStr = "$" + myFormat.format(cash);
        font = new Font(FONT_PATH, LARGE_FONT);
        font.drawString(cashStr, WIDTH - font.getWidth(cashStr) - 50, 65);


    }

    private void drawStatusPanel(Font font, Image statusPanel, WaveManager waveManager) {
        statusPanel.drawFromTopLeft(ORIGIN, HEIGHT - statusPanel.getHeight());

        // Dynamically updates content based on the status of the game
        String waveNum = "Wave: " + waveManager.getCurrentWaveNum();



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

    public void setStatus() {
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


    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }

    public List<Projectile> getProjectileList() {
        return projectileList;
    }

    public void nextLevel(Input input) {


        if (levelIndex < levelsList.size() - 1) {
            this.levelIndex++;
        } else {
            // Cover the screen

            setIsWinner(true);


            // TODO: any key to exit
        }
    }

    public void resetCash() {
        this.cash = INITAL_CASH;
    }

    public void setIsWaveInProg(boolean isWaveInProg) {
        this.isWaveInProg = isWaveInProg;
    }

    public void setIsAwaiting(boolean isAwaiting) {
        this.isAwaiting = isAwaiting;
    }

    public static Input getUserInput() {
        return userInput;
    }

    public void setIsPlacing(boolean isPlacing) {
        this.isPlacing = isPlacing;
    }
}