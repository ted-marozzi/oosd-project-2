public class StopWatch {

    private long start;
    private static final long MS = 1000000;

    public StopWatch()
    {
        start = System.nanoTime();
    }

    public long stop()
    {
        return (System.nanoTime() - start)/MS;
    }

    public void reset()
    {
        start = System.nanoTime();
    }
}
