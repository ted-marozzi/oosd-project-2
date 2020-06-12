import bagel.DrawOptions;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;

public class AirSupport extends Tower {
    // Constants
    private static final String IMG_PATH = "res/images/airsupport.png";
    private static final int PRICE = 500;
    private static final int SPEED = 3;
    private static final int START_OFFSET = - 10;
    private static final int TO_MS = 1000;

    private double dropTime;

    // Boolean oscillator
    private static boolean isHorizontal = true;
    // Lets each individual plane assume a direction
    private boolean isHorizontalIndividual = true;
    // Checks if plane has been placed anywhere
    private boolean placed = false;

    private DrawOptions rotation;
    private StopWatch stopWatch;
    private final ArrayList<Bomb> bombs = new ArrayList<>();

    // Constructor
    public AirSupport(Point pos) {
        super(pos, IMG_PATH, PRICE);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public AirSupport create(Point pos)
    {
        return new AirSupport(pos);
    }

    @Override
    public void update(List<Slicer> slicerList, ShadowDefend shadowDefend)
    {
        // Only run once per AirSupport
        if(!placed)
        {
            stopWatch = new StopWatch();
            rotation = new DrawOptions();
            isHorizontalIndividual = isHorizontal;
            if(isHorizontalIndividual)
            {
                setPos(new Point(START_OFFSET, ShadowDefend.getUserInput().getMouseY()));
            }
            else
            {
                setPos(new Point(ShadowDefend.getUserInput().getMouseX(), START_OFFSET ));
            }

            placed = true;

            isHorizontal = !isHorizontal;
            dropTime = dropTime();
        }

        // Determines the pos of the plane each update
        // If its a horizontal plane
        Point newPos;
        if(isHorizontalIndividual)
        {
            rotation.setRotation(Math.PI/2);
            newPos = new Point( getPos().x +  SPEED * ShadowDefend.getTimeScale(), getPos().y);
        }
        // If its a vert plane
        else
        {
            rotation.setRotation(Math.PI);
            newPos = new Point( getPos().x , getPos().y +  SPEED * ShadowDefend.getTimeScale());
        }
        // Moves the plane
        setPos(newPos);

        draw(rotation);
        // Bomb dropping logic
        if(stopWatch.lapMS() >= dropTime/ShadowDefend.getTimeScale() && shadowDefend.inPlay(this.getPos(), START_OFFSET))
        {
            // drop
            stopWatch.reset();
            dropTime = dropTime();

            bombs.add(new Bomb(newPos));
        }
        // Remove bombs as necessary
        bombs.removeIf(bomb -> bomb.drop(slicerList, shadowDefend));
    }
    // TO_MS converts the random time from seconds to ms
    private double dropTime()
    {
        return (Math.random() + 1)*TO_MS;
    }

    public boolean isBombsEmpty()
    {
        return bombs.size() == 0;
    }


}

