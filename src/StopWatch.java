public class StopWatch {

    private long start;

    public StopWatch()
    {
        start = System.nanoTime();
    }

    public long stop()
    {
        return System.nanoTime() - start;
    }

    public void reset()
    {
        start = System.nanoTime();
    }
}
