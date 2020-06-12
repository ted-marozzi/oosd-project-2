public class StopWatch {

    // Timing class to abstract from performing the difference calculation
    private long start;
    private static final long MS = 1000000;

    public StopWatch()
    {
        start = System.nanoTime();
    }

    public long lapMS()
    {
        return (System.nanoTime() - start)/MS;
    }

    public void reset()
    {
        start = System.nanoTime();
    }
}
