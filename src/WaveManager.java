import bagel.Input;
import bagel.Keys;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public final class WaveManager {

    private static ArrayList<WaveEvent> waveEvents = new ArrayList<>();
    private static int waveEventIndex = 0;
    private static final String SPAWN = "spawn";
    private static final String DELAY = "delay";

    private static final String WAVE_PATH = "res/levels/waves.txt";
    private static int currentWaveNum = 0;
    private static boolean endOfWave = true;


    // TODO look into making this static with  no constructor
    public WaveManager() {

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

    public WaveEvent getWaveEvent(int index) {

        return waveEvents.get(index);
    }
    // The current wave event
    public static WaveEvent getCurrentWaveEvent() {

        return waveEvents.get(waveEventIndex);
    }

    public static int getWaveEventIndex() {
        return waveEventIndex;
    }
    // begins a single wave event
    public void beginWaveEvent()
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
    public void endWaveEvent()
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


    public void updateWaveEvent() {
        // Ensures after all waves are finished we don't try to update
        if(! (waveEventIndex == waveEvents.size() - 1) )
        {
            WaveEvent wave = waveEvents.get(waveEventIndex);
            if(wave.getAction().equals(SPAWN))
            {

                if(wave.checkTimer() >= wave.getSpawnDelay()/ShadowDefend.getTimeScale() && wave.getNumSpawned() < wave.getNumToSpawn())
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
                if(wave.checkTimer() >= wave.getDelay()/ShadowDefend.getTimeScale())
                {
                    endWaveEvent();

                }

            }

        }


    }

    // Using this to avoid having to copys of this static
    public static String getDELAY() {
        return DELAY;
    }

    public static String getSPAWN() {
        return SPAWN;
    }
    public int getNextWaveEventNum()
    {
        return waveEvents.get(waveEventIndex + 1).getWaveNumber();
    }
    public int getWaveEventNum()
    {
        return waveEvents.get(waveEventIndex).getWaveNumber();
    }

    public static int getCurrentWaveNum() {
        return currentWaveNum;
    }
    // begins a wave
    public void beginWave(Input input)
    {
        if(input.wasPressed(Keys.S) && !getCurrentWaveEvent().getInProgress() && Slicer.getSlicerList().isEmpty())
        {
            Level.setStatus("Wave In Progress");
            beginWaveEvent();
        }

    }

    public boolean getEndOfWave()
    {
        return endOfWave;
    }

}


