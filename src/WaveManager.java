import bagel.Input;
import bagel.Keys;

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

    private int currentWaveNum = 0;
    private boolean endOfWave = false;



    private static WaveManager _instance = null;
    // Can only be one waveManger and we want users to access through the instance
    public static WaveManager getInstance()
    {
        if(_instance == null)
        {
            return _instance = new WaveManager();
        }
        return _instance;
    }


    private WaveManager() {

        FileInputStream wavesStream = null;
        try {
            wavesStream = new FileInputStream(WAVE_PATH);

        } catch (IOException e) {
            e.printStackTrace();
        }

        assert wavesStream != null;
        Scanner sc = new Scanner(wavesStream);
        while (sc.hasNextLine()) {
            waveEvents.add(new WaveEvent(sc.nextLine()));
        }


    }

    // The current wave event
    public WaveEvent getCurrentWaveEvent() {

        return waveEvents.get(waveEventIndex);
    }

    // begins a single wave event
    private void beginWaveEvent()
    {

        WaveEvent wave = waveEvents.get(waveEventIndex);
        wave.setInProgress(true);
        wave.startTimer();
        endOfWave = true;

        if(wave.getAction().equals(SPAWN))
        {
            wave.spawnSlicer();
        }
        currentWaveNum++;

    }
    // Ends a single wave event
    private void endWaveEvent()
    {
        WaveEvent wave = waveEvents.get(waveEventIndex);
        wave.setInProgress(false);
        waveEventIndex++;

        if(waveEvents.get(waveEventIndex).getWaveNumber() == waveEvents.get(waveEventIndex - 1).getWaveNumber())
        {
            beginWaveEvent();
            currentWaveNum--;
        }
        else
        {
            endOfWave = true;


        }

    }


    public void updateWaveEvent(double timeScale) {
        // Ensures after all waves are finished we don't try to update
        if(! (waveEventIndex == waveEvents.size() - 1) )
        {
            WaveEvent wave = waveEvents.get(waveEventIndex);
            if(wave.getAction().equals(SPAWN))
            {

                if(wave.checkTimer() >= wave.getSpawnDelay()/timeScale && wave.getNumSpawned() < wave.getNumToSpawn())
                {
                    wave.resetTimer();
                    wave.spawnSlicer();
                }

                if(wave.getNumSpawned()  >= wave.getNumToSpawn() )
                {
                    endWaveEvent();
                }


            }

            if(wave.getAction().equals(DELAY))
            {
                if(wave.checkTimer() >= wave.getDelay()/timeScale)
                {
                    endWaveEvent();

                }

            }

        }


    }


    // begins a wave
    public void beginWave(Input input)
    {
        if(input.wasPressed(Keys.S) && !getCurrentWaveEvent().getInProgress() && ShadowDefend.getInstance().getSlicerList().isEmpty())
        {
            ShadowDefend.getInstance().setStatus("Wave In Progress");
            beginWaveEvent();
        }

    }

    public boolean getEndOfWave()
    {
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
}


