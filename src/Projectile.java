import bagel.Image;
import bagel.util.Point;

public class Projectile
{
    private final int damage;
    private final Slicer slicer;
    private Point pos;
    private final Image img;
    private static final int SPEED = 10;


    public Projectile(Point pos, String imgPath, int damage, Slicer slicer)
    {
        this.slicer = slicer;
        this.damage = damage;
        this.img = new Image(imgPath);
        this.pos = pos;
        img.draw(pos.x, pos.y);
    }

    public boolean update(double timeScale, ShadowDefend shadowDefend)
    {
        pos = pos.asVector().add(slicer.getPos().asVector().sub(this.pos.asVector()).normalised().mul(SPEED*timeScale)).asPoint();

        if(this.pos.distanceTo(slicer.getPos()) < 7*timeScale)
        {
            slicer.dealDamage(this.damage, shadowDefend);
            return false;
        }

        img.draw(pos.x, pos.y);
        return true;
    }





}
