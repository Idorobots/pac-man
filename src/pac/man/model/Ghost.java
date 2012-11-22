package pac.man.model;

import android.graphics.Point;
import pac.man.gfx.Animation;
import pac.man.util.Vector;

public class Ghost extends Character {
    private Vector target;

    public Ghost(Vector position, Animation[] animations) {
        super(new Vector(animations[0].getWidth(), animations[0].getHeight()), position, animations);
    }

    public Vector getTarget() {
        return target;
    }
}
