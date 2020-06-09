import bagel.DrawOptions;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.List;

public abstract class GroundTower extends Tower
{

    private int range, damage, coolDown;
    StopWatch stopWatch;
    Point pos;
    String projectilePath;
    DrawOptions drawOptions;


    protected GroundTower(Point pos, String imgPath, int price, int range, int damage, int coolDown, String projectilePath)
    {
        super(pos, imgPath, price);
        this.range = range;
        this.damage = damage;
        this.coolDown = coolDown;
        this.pos = pos;
        this.stopWatch = new StopWatch();
        this.projectilePath = projectilePath;
        this.drawOptions = new DrawOptions();

    }



    public void update(List<Slicer> slicerList, ShadowDefend shadowDefend) {


        for(Slicer slicer: slicerList)
        {
            if(slicer.getPos().distanceTo(this.pos) <= range && this.stopWatch.lap() >= coolDown/shadowDefend.getTimeScale())
            {
                drawOptions.setRotation(calcRotation(slicer));
                fire(shadowDefend, slicer);
                stopWatch.reset();
            }

        }
        draw(drawOptions);
    }

    private void fire(ShadowDefend shadowDefend, Slicer slicer)
    {

        shadowDefend.getProjectileList().add(new Projectile(pos, projectilePath, damage, slicer));
    }

    private double calcRotation(Slicer slicer)
    {
        Vector2 directionVec = slicer.getPos().asVector().sub(this.pos.asVector());

        // Calc rotation
        double rotation = Math.atan(directionVec.y/directionVec.x)+Math.PI/2;
        // As atan only guaranteed to work for positive x
        if(directionVec.asPoint().x < 0)
            rotation = rotation-Math.PI;

        return rotation;
    }

}
