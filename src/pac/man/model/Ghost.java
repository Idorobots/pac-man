package pac.man.model;

import android.graphics.Point;
import pac.man.gfx.Animation;

public class Ghost extends Character {
    private Point target;

    public Ghost(Point position, Animation[] animations) {
        super(position, animations);
    }

    public Point getTarget() {
        return target;
    }
}
