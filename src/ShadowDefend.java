// Bagel Imports
import bagel.AbstractGame;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
// Java Imports
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;

public class ShadowDefend extends AbstractGame {
    // Timing
    private static int timeScale = 1;
    FpsCalc fpsCalc;

    private static WaveManager waveManager;

    // Constructor
    public ShadowDefend() {
        Level.loadLevels();
        waveManager = new WaveManager();
        fpsCalc = new FpsCalc();
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDefend game = new ShadowDefend();
        game.run();
    }

    @Override
    public void update(Input input) {
        // Calculates the time scale
        calcTimeScale(input);
        // Draws the current level
        Level.getCurrentLevel().draw();

        if(input.wasPressed(Keys.S) && !waveManager.getCurrentWaveEvent().getInProgress() && Slicer.getSlicerList().isEmpty())
        {
            waveManager.beginWaveEvent();
        }

        if( waveManager.getCurrentWaveEvent().getInProgress() == true )
            waveManager.updateWaveEvent();
        Slicer.update();

        fpsCalc.calc();
    }

    private void calcTimeScale(Input input)
    {
        // Uses keyboard input to determine speed of the game
        if(input.wasPressed(Keys.L))
            timeScale ++;
        if(input.wasPressed(Keys.K) && timeScale > 1)
            timeScale --;

    }

    public static int getTimeScale() {
        return timeScale;
    }
}