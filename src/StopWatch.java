public class StopWatch {

    private long start;
    private static final long MS = 1000000;

    public StopWatch()
    {
        start = System.nanoTime();
    }

    public long lap()
    {
        return (System.nanoTime() - start)/MS;
    }
    public long stop()
    {
        long time = (System.nanoTime() - start)/MS;
        reset();
        return time;
    }

    public void reset()
    {
        start = System.nanoTime();
    }
}
