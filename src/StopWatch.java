/**
 * A class to simplify timing.
 */
public class StopWatch {

    // Timing class to abstract from performing the difference calculation
    private long start;
    private static final long MS = 1000000;

    /**
     * Starts a timer.
     */
    public StopWatch()
    {
        start = System.nanoTime();
    }

    /**
     * @return The time difference from the creation of StopWatch in milliseconds.
     */
    public long lapMS()
    {
        return (System.nanoTime() - start)/MS;
    }

    /**
     * Resets the timer.
     */
    public void reset()
    {
        start = System.nanoTime();
    }
}
