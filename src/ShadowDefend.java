// Bagel Imports
import bagel.AbstractGame;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
// Java Imports
import java.util.ArrayList;
import java.util.Iterator;

public class ShadowDefend extends AbstractGame {
    // Constants
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 800;
    private static final int ORIGIN = 0;
    private long frameCount = 0;
    private static long fpsStart;
    private double waveCount = 0;
    private long fps;

    // Paths
    private static final String SLICER_PATH = "res/images/slicer.png";
    private static final String LEVEL_ONE_PATH = "res/levels/1.tmx";
    private static final double SLICER_DELAY = 5;
    private static final double NUM_SLICERS = 5;

    // Timing
    private static int timeScale = 1;
    private static final double NS = 1000000000.0;

    // Lists to keep track of how many levels and slicers there are
    private static ArrayList<Enemy> slicerList = new ArrayList<>();
    private static ArrayList<Level> levelList = new ArrayList<>();

    // Constructor
    public ShadowDefend() {
        // Do this for all levels
        levelList.add(new Level(LEVEL_ONE_PATH));
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDefend game = new ShadowDefend();
        fpsStart = System.nanoTime();
        game.run();

    }

    @Override
    public void update(Input input) {
        // Will add logic to modify current level if needed in future
        int currentLevel = 0;
        // List of different levels, currently only has one level
        levelList.get(currentLevel).getMap().draw(ORIGIN,ORIGIN,ORIGIN,ORIGIN,WIDTH,HEIGHT);
        // Start current level wave
        levelList.get(currentLevel).beginWave(input);
        // if a wave is going, keep it going
        if (levelList.get(currentLevel).getIsWave())
            wave(currentLevel, input);

        calcFps();
    }

    // Tracks fps to enable the game to run independent of fps except for the slicer movement
    //      which we were forced to use. Personally I think this is bad design especially with
    //      the fps bug in the bagel graphics library. :(
    void calcFps()
    {
        frameCount++;
        long fpsFinish = System.nanoTime();
        long fpsDiff = (long) ((fpsFinish - fpsStart) / NS);
        // Avoids dividing by zero
        if(fpsDiff !=0)
        {
            fps = frameCount/ fpsDiff;
            //System.out.println(fps);
            // Running at 144 fps on my 144 hz monitor :(
        }
    }

    void wave(int currentLevel, Input input)
    {
        // Ends the wave if the slicer list is empty, if we have multiple enemies we will need a list of all enemies
        levelList.get(currentLevel).endWave();
        // Spawns a new slicer if f/s*s = f
        if(waveCount >= fps*SLICER_DELAY && Enemy.getNumEnemies() < NUM_SLICERS)
        {
            // Reset waveCount
            waveCount = 0;
            // Add a new slicer
            addSlicer(levelList.get(currentLevel).getStart());
        }

        // Every slicer perform the update and remove dead enemies
        // Allows for removing enemies if player defeats them also
        Iterator<Enemy> iter = slicerList.iterator();
        while(iter.hasNext())
        {
            Enemy slicer = iter.next();
            slicer.update(timeScale, levelList.get(currentLevel).getPolyLines());
            if(!slicer.isAlive())
            {
                iter.remove();
            }
        }
        // Wave count here helps release slicers every 5 seconds/time scale
        timeScale = calcTimeScale(timeScale, input);
        waveCount = waveCount + timeScale;
    }

    private int calcTimeScale(int timeScale, Input input)
    {
        // Uses keyboard input to determine speed of the game
        if(input.wasPressed(Keys.L))
            timeScale ++;
        if(input.wasPressed(Keys.K) && timeScale > 1)
            timeScale --;
        return timeScale;

    }

    // List of slicers
    public static ArrayList<Enemy> getSlicerList()
    {
        return slicerList;
    }

    // Add a slicer
    public static void addSlicer(Point start)
    {
        slicerList.add(new Enemy(SLICER_PATH, start));
    }

}