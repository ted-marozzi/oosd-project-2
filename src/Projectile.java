import bagel.Image;
import bagel.util.Point;

// Shot by tank and super tank
public class Projectile
{
    private final int damage;
    private final Slicer slicer;
    private Point pos;
    private final Image img;
    private static final int SPEED = 10;
    private static final int EPSILON = 7;

    public Projectile(Point pos, String imgPath, int damage, Slicer slicer)
    {
        this.slicer = slicer;
        this.damage = damage;
        this.img = new Image(imgPath);
        this.pos = pos;
        img.draw(pos.x, pos.y);
    }

    // Moves the projectile towards the updated slicer pos
    public boolean update(double timeScale, ShadowDefend shadowDefend)
    {
        pos = pos.asVector().add(slicer.getPos().asVector().sub(this.pos.asVector()).normalised().mul(SPEED*timeScale)).asPoint();
        // checks if a slicer is hit
        if(this.pos.distanceTo(slicer.getPos()) < EPSILON*timeScale)
        {
            slicer.dealDamage(this.damage, shadowDefend);
            return false;
        }

        img.draw(pos.x, pos.y);
        return true;
    }

}
