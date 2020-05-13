public class WaveEvent {
    private String waveEvent;
    private String[] waveEventSplit;
    private int waveNumber;
    private String action;
    private int delay;
    private int numToSpawn;
    private String slicerType;
    private int spawnDelay;
    private static final String SPAWN = "spawn";
    private static final String DELAY = "delay";
    private boolean inProgress = false;
    private StopWatch stopWatch;

    public WaveEvent(String waveEvent)
    {
        this.waveEventSplit = waveEvent.split(",");
        this.waveNumber = Integer.parseInt(waveEventSplit[0]);
        this.action = waveEventSplit[1];

        if(action.equals(SPAWN))
        {
            this.numToSpawn = Integer.parseInt(waveEventSplit[2]);
            this.slicerType = waveEventSplit[3];
            this.spawnDelay = Integer.parseInt(waveEventSplit[4]);
        }
        else if(action.equals(DELAY))
        {
            this.delay = Integer.parseInt(waveEventSplit[2]);
        }
    }

    public void startTimer()
    {
        stopWatch = new StopWatch();
    }

    public long checkTimer()
    {
        return stopWatch.stop();
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public String getAction() {
        return action;
    }

    public boolean getInProgress()
    {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public void spawnSlicer()
    {
        if(this.slicerType.equals("slicer"))
        {
            Slicer slicer = new RegularSlicer(Level.getCurrentLevel().getStart());
        }

        if(this.slicerType.equals("superslicer"))
        {
            Slicer slicer = new SuperSlicer(Level.getCurrentLevel().getStart());
        }

        if(this.slicerType.equals("megaslicer"))
        {
            Slicer slicer = new MegaSlicer(Level.getCurrentLevel().getStart());
        }
        if(this.slicerType.equals("apexslicer"))
        {
            Slicer slicer = new ApexSlicer(Level.getCurrentLevel().getStart());
        }

    }

    public void resetTimer()
    {
        stopWatch.reset();
    }


    public int getSpawnDelay() {
        return spawnDelay;
    }
}
