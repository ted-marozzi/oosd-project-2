import java.util.ArrayList;

public class Wave {

    private ArrayList<String> waveEvents;
    private static int waveNumber;

    public Wave(ArrayList<String> waveEvents)
    {

        this.waveNumber = Integer.parseInt(String.valueOf(waveEvents.get(0).charAt(0)));
        this.waveEvents = waveEvents;


    }
}
