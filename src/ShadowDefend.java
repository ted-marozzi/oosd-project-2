// Bagel Imports
import bagel.AbstractGame;
import bagel.Input;
import bagel.Keys;
// Java Imports


public class ShadowDefend extends AbstractGame {
    // Timing
    private static double timeScale = 1;
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

        waveManager.beginWave(input);

        if( waveManager.getCurrentWaveEvent().getInProgress() == true )
            waveManager.updateWaveEvent();
        Slicer.update();
        if(waveManager.getEndOfWave() == true && Slicer.getSlicerList().isEmpty())
            Level.setStatus(Level.getAWAITING());

        Level.drawPanels();

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

    public static double getTimeScale() {
        return timeScale;
    }
}