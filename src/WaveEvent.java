/**
 * Class to contain the instructions for a waveEvent.
 */
// Class to hold one line in the waves.txt file
public class WaveEvent {

    private final int waveNumber;
    private final String action;
    private String slicerType;
    private int delay;
    private int numToSpawn;
    private int spawnDelay;
    private int numSpawned = 0;
    private boolean inProgress = false;
    private StopWatch stopWatch;

    /**
     * @param waveEvent The text file string line containing the waveEvent.
     */
    // Constructor, assigns txt file to variables
    public WaveEvent(String waveEvent)
    {
        String[] waveEventSplit = waveEvent.split(",");
        this.waveNumber = Integer.parseInt(waveEventSplit[0]);
        this.action = waveEventSplit[1];

        if(action.equals(ShadowDefend.getSPAWN()))
        {
            this.numToSpawn = Integer.parseInt(waveEventSplit[2]);
            this.slicerType = waveEventSplit[3];
            this.spawnDelay = Integer.parseInt(waveEventSplit[4]);
        }
        else if(action.equals(ShadowDefend.getDELAY()))
        {
            this.delay = Integer.parseInt(waveEventSplit[2]);
        }
    }

    /**
     * @param shadowDefend The game.
     */
    // Spawns the appropriate slicer
    public void spawnSlicer(ShadowDefend shadowDefend)
    {
        //noinspection IfCanBeSwitch
        if(this.slicerType.equals("slicer"))
        {
            shadowDefend.addSlicer(new RegularSlicer(shadowDefend.getCurrentLevel().getStart()));
        }
        else if (this.slicerType.equals("superslicer"))
        {
            shadowDefend.addSlicer(new SuperSlicer(shadowDefend.getCurrentLevel().getStart()));
        }
        else if(this.slicerType.equals("megaslicer"))
        {
            shadowDefend.addSlicer(new MegaSlicer(shadowDefend.getCurrentLevel().getStart()));
        }
        else if(this.slicerType.equals("apexslicer"))
        {
            shadowDefend.addSlicer(new ApexSlicer(shadowDefend.getCurrentLevel().getStart()));
        }
        numSpawned++;

    }

    /**
     * Creates a stopWatch object contained inside the WaveEvent.
     */
    // Timing
    public void startTimer()
    {
        stopWatch = new StopWatch();
    }

    /**
     * @return The time on the stopWatch.
     */
    public long checkTimer()
    {
        return stopWatch.lapMS();
    }


    /**
     * @return The number of slicers to spawn.
     */
    public int getNumToSpawn() {
        return numToSpawn;
    }

    /**
     * Restarts the timer.
     */
    public void resetTimer()
    {
        stopWatch.reset();
    }


    /**
     * @return The delay between slicer spawns.
     */
    public int getSpawnDelay() {
        return spawnDelay;
    }

    /**
     * @return The delay between slicer spawns.
     */
    public long getDelay() {
        return delay;
    }

    /**
     * @return The number spawned.
     */
    public int getNumSpawned() {
        return numSpawned;
    }

    /**
     * @param numSpawned The number spawned.
     */
    public void setNumSpawned(int numSpawned)
    {
        this.numSpawned = numSpawned;
    }

    /**
     * @return The wave number of the event.
     */
    public int getWaveNumber() {
        return waveNumber;
    }

    /**
     * @return The action of the event.
     */
    public String getAction() {
        return action;
    }

    /**
     * @return If the wave event is in progress or not.
     */
    public boolean getInProgress()
    {
        return inProgress;
    }

    /**
     * @param inProgress If the wave event is in progress or not.
     */
    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }
}
