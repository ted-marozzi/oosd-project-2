import bagel.DrawOptions;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * The plane tower class, limited life time and drops bombs.
 */
public class AirSupport extends Tower {
    // Constants
    private static final String IMG_PATH = "res/images/airsupport.png";
    private static final int PRICE = 500;
    private static final int SPEED = 3;
    private static final int START_OFFSET = -10;
    private static final int TO_MS = 1000;
    private static final boolean INITIAL_DIRECTION = true;
    // Boolean oscillator
    private static boolean isHorizontal = INITIAL_DIRECTION;
    private final ArrayList<Bomb> bombs = new ArrayList<>();
    private double dropTime;
    // Lets each individual plane assume a direction
    private boolean isHorizontalIndividual = INITIAL_DIRECTION;
    // Checks if plane has been placed anywhere
    private boolean placed = false;
    private DrawOptions rotation;
    private StopWatch stopWatch;

    /**
     * @param pos The position to spawn the plane at used in buy panel.
     */
    // Constructor
    public AirSupport(Point pos) {
        super(pos, IMG_PATH, PRICE);
    }

    /**
     * Resets the initial direction.
     */
    public static void resetIsHorizontal() {
        isHorizontal = INITIAL_DIRECTION;
    }

    /**
     * @param obj checks weather the object passed in is the same as this.
     * @return A boolean determining if the objects were equal or not.
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * @param pos creates a new plane object as pos.
     * @return the plane object.
     */
    @Override
    public AirSupport create(Point pos) {
        return new AirSupport(pos);
    }

    /**
     * @param slicerList list of slicers.
     * @param shadowDefend the game to update the planes.
     */
    @Override
    public void update(List<Slicer> slicerList, ShadowDefend shadowDefend) {
        // Only run once per AirSupport
        if (!placed) {
            stopWatch = new StopWatch();
            rotation = new DrawOptions();
            isHorizontalIndividual = isHorizontal;
            if (isHorizontalIndividual) {
                setPos(new Point(START_OFFSET, ShadowDefend.getUserInput().getMouseY()));
            } else {
                setPos(new Point(ShadowDefend.getUserInput().getMouseX(), START_OFFSET));
            }

            placed = true;
            // Toggles the boolean
            isHorizontal = !isHorizontal;
            dropTime = dropTime();
        }

        // Determines the pos of the plane each update
        // If its a horizontal plane
        Point newPos;
        if (isHorizontalIndividual) {
            rotation.setRotation(Math.PI / 2);
            newPos = new Point(getPos().x + SPEED * ShadowDefend.getTimeScale(), getPos().y);
        }
        // If its a vert plane
        else {
            rotation.setRotation(Math.PI);
            newPos = new Point(getPos().x, getPos().y + SPEED * ShadowDefend.getTimeScale());
        }
        // Moves the plane
        setPos(newPos);

        draw(rotation);
        // Bomb dropping logic
        if (stopWatch.lapMS() >= dropTime / ShadowDefend.getTimeScale() && shadowDefend.inPlay(this.getPos(), START_OFFSET)) {
            // drop
            stopWatch.reset();
            dropTime = dropTime();

            bombs.add(new Bomb(newPos));
        }
        // Remove bombs as necessary
        bombs.removeIf(bomb -> bomb.drop(slicerList, shadowDefend));
    }

    // TO_MS converts the random time from seconds to ms
    private double dropTime() {
        return (Math.random() + 1) * TO_MS;
    }

    /**
     * @return a boolean determining whether or not the bombs list is empty or not.
     */
    public boolean isBombsEmpty() {
        return bombs.size() == 0;
    }

}

