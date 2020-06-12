import bagel.Input;
import bagel.Keys;
import org.lwjgl.system.CallbackI;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public final class WaveManager {

    private ArrayList<WaveEvent> waveEvents = new ArrayList<>();
    private int waveEventIndex = 0;
    private static final String SPAWN = "spawn";
    private static final String DELAY = "delay";
    private static final String WAVE_PATH = "res/levels/waves.txt";

    private int currentWaveNum = 1;
    private boolean endOfWave = false;

    public WaveManager() {

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

    public void nextWave()
    {
        this.currentWaveNum++;
    }

    // The current wave event
    public WaveEvent getCurrentWaveEvent() {
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


    public void updateWaveEvent(double timeScale, ShadowDefend shadowDefend) {
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


            shadowDefend.setLevelComplete(true);
            for( WaveEvent wave: waveEvents)
            {
                wave.setNumSpawned(0);
            }

        }

    }

    public void setWaveEventIndex(int waveEventIndex) {
        this.waveEventIndex = waveEventIndex;
    }

    // begins a wave
    public void beginWave(Input input, ShadowDefend shadowDefend) {
        if (input.wasPressed(Keys.S) && !getCurrentWaveEvent().getInProgress() && shadowDefend.getSlicerList().isEmpty()) {
            shadowDefend.setIsWaveInProg(true);
            beginWaveEvent(shadowDefend);
        }

    }

    public boolean getEndOfWave() {
        return endOfWave;
    }

    public void setEndOfWave(boolean endOfWave) {
        this.endOfWave = endOfWave;
    }

    // Using this to avoid having to copy's of this static
    public static String getDELAY() {
        return DELAY;
    }

    public static String getSPAWN() {
        return SPAWN;
    }

    public int getCurrentWaveNum() {
        return currentWaveNum;
    }

    public ArrayList<WaveEvent> getWaveEvents() {
        return waveEvents;
    }

    public int getWaveEventIndex() {
        return waveEventIndex;
    }
}


