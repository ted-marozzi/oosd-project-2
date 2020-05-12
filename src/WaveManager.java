import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public final class WaveManager {

    ArrayList<String> waveEvents = new ArrayList<>();

    public WaveManager(FileInputStream waves)
    {
        Scanner sc = new Scanner(waves);
        while(sc.hasNextLine())
        {
            waveEvents.add(sc.nextLine());

        }
    }

    public String getWaveEvent(int i)
    {
        return waveEvents.get(i);
    }
}


