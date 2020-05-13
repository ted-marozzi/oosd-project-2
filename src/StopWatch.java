public class StopWatch {

    private long start;

    public StopWatch()
    {
        start = System.nanoTime();
    }

    public long stop()
    {
        return start - System.nanoTime();
    }

    public void reset()
    {
        start = System.nanoTime();
    }
}
