public class WaveEvent {

    private int waveNumber;
    private String action;
    private String slicerType;
    private int delay;
    private int numToSpawn;

    private int spawnDelay;
    private int numSpawned = 0;

    private boolean inProgress = false;
    private StopWatch stopWatch;

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

    public void startTimer()
    {
        stopWatch = new StopWatch();
    }

    public long checkTimer()
    {
        return stopWatch.lap();
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

    public void spawnSlicer(ShadowDefend shadowDefend)
    {
        if(this.slicerType.equals("slicer"))
        {
            new RegularSlicer(shadowDefend.getCurrentLevel().getStart(), shadowDefend);
        }
        else if (this.slicerType.equals("superslicer"))
        {
            new SuperSlicer(shadowDefend.getCurrentLevel().getStart(), shadowDefend);
        }
        else if(this.slicerType.equals("megaslicer"))
        {
            new MegaSlicer(shadowDefend.getCurrentLevel().getStart(), shadowDefend);
        }
        else if(this.slicerType.equals("apexslicer"))
        {
            new ApexSlicer(shadowDefend.getCurrentLevel().getStart(), shadowDefend);
        }
        numSpawned++;

    }



    public int getNumToSpawn() {
        return numToSpawn;
    }

    public void resetTimer()
    {
        stopWatch.reset();
    }


    public int getSpawnDelay() {
        return spawnDelay;
    }

    public long getDelay() {
        return delay;
    }

    public int getNumSpawned() {
        return numSpawned;
    }
    public void setNumSpawned(int numSpawned)
    {
        this.numSpawned = numSpawned;
    }
}
