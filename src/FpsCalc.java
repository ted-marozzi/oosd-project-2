public final class FpsCalc {
    private long frameCount = 0;
    private static long fpsStart;
    private static final double NS = 1000000000.0;
    private long fps;

    public FpsCalc()
    {
        fpsStart = System.nanoTime();
    }


    public void calc()
    {
        frameCount++;
        long fpsFinish = System.nanoTime();
        long fpsDiff = (long) ((fpsFinish - fpsStart) / NS);
        // Avoids dividing by zero
        if(fpsDiff !=0)
        {
            fps = frameCount / fpsDiff;
            // System.out.println(fps);
            // Running at 144 fps on my 144 hz monitor :(
        }
    }
    // Returns fps
    public long getFps() {
        return fps;
    }

    public void printFps()
    {
        System.out.println(fps);
    }
}
