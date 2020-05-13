import bagel.Input;
import bagel.Keys;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static bagel.Window.close;

public final class WaveManager {

    ArrayList<WaveEvent> waveEvents = new ArrayList<>();
    private final int currentWaveEventIndex = 0;
    private static final String SPAWN = "spawn";

    private static final String WAVE_PATH = "res/levels/waves.txt";
    private static boolean isWave = false;


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






    public void beginWaveEvent()
    {
        WaveEvent wave = waveEvents.get(currentWaveEventIndex);
        wave.setInProgress(true);
        wave.startTimer();

        if(wave.getAction().equals(SPAWN))
        {
            wave.spawnSlicer();
        }

        return;
    }


    public void updateWaveEvent() {

        WaveEvent wave = waveEvents.get(currentWaveEventIndex);
        if(wave.getAction().equals(SPAWN))
        {

            if(wave.checkTimer() >= wave.getSpawnDelay())
            {
                wave.resetTimer();
                wave.spawnSlicer();
            }

        }

        Slicer.update();

    }
}


