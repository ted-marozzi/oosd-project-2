import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public final class WaveManager {

    ArrayList<WaveEvent> waveEvents = new ArrayList<>();
    private static int currentWaveEventIndex = 0;
    private static final String SPAWN = "spawn";
    private static final String DELAY = "delay";

    private static final String WAVE_PATH = "res/levels/waves.txt";
    private static final boolean isWave = false;


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
    public WaveEvent getCurrentWaveEvent() {

        return waveEvents.get(currentWaveEventIndex);
    }

    public static int getCurrentWaveEventIndex() {
        return currentWaveEventIndex;
    }

    public void beginWaveEvent()
    {
        WaveEvent wave = waveEvents.get(currentWaveEventIndex);
        wave.setInProgress(true);
        wave.startTimer();

        if(wave.getAction().equals(SPAWN))
        {
            wave.spawnSlicer();
        }

    }

    public void endWaveEvent()
    {
        WaveEvent wave = waveEvents.get(currentWaveEventIndex);
        wave.setInProgress(false);
        currentWaveEventIndex++;


    }


    public void updateWaveEvent() {
        if(! (currentWaveEventIndex == waveEvents.size() - 1) )
        {

            WaveEvent wave = waveEvents.get(currentWaveEventIndex);
            if(wave.getAction().equals(SPAWN))
            {
                //System.out.println(wave.checkTimer());
                if(wave.checkTimer() >= wave.getSpawnDelay()/ShadowDefend.getTimeScale() && Slicer.getSlicerList().size() < wave.getNumToSpawn())
                {
                    wave.resetTimer();
                    wave.spawnSlicer();
                }

                if(Slicer.getSlicerList().size() >= wave.getNumToSpawn() )
                    endWaveEvent();

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

    public static String getDELAY() {
        return DELAY;

    }

    public static String getSPAWN() {
        return SPAWN;
    }
}


