import bagel.DrawOptions;
import bagel.Keys;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;

public class AirSupport extends Tower {

    private static final String IMG_PATH = "res/images/airsupport.png";
    private static final int PRICE = 500;
    private static final int SPEED = 3;
    private static boolean isHorizontal = true;
    private boolean isHorizontalIndividual = true;
    private boolean placed = false;
    private DrawOptions rotation;
    private StopWatch stopWatch;
    private double dropTime;
    private ArrayList<Bomb> bombs = new ArrayList<>();


    private static final int START_OFFSET = - 10;


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

        Point newPos;
        if(isHorizontalIndividual)
        {
            rotation.setRotation(Math.PI/2);
            newPos = new Point( getPos().x +  SPEED * ShadowDefend.getTimeScale(), getPos().y);
        }
        else
        {
            rotation.setRotation(Math.PI);
            newPos = new Point( getPos().x , getPos().y +  SPEED * ShadowDefend.getTimeScale());
        }

        setPos(newPos);

        draw(rotation);

        if(stopWatch.lap() >= dropTime)
        {
            // drop
            stopWatch.reset();
            dropTime = dropTime();

            bombs.add(new Bomb(newPos));
        }

        bombs.removeIf(bomb -> bomb.drop(slicerList));


    }

    private double dropTime()
    {
        return (Math.random() + 1)*1000;
    }

}

