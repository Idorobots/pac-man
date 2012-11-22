package pac.man.model;

import android.graphics.Point;
import pac.man.gfx.Animation;

public class Ghost extends Character {
    private Point target;

    public Ghost(Point position, Animation[] animations) {
        super(new Point(animations[0].getWidth(), animations[0].getHeight()), position, animations);
    }

    public Point getTarget() {
        return target;
    }
}
