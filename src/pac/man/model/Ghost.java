package pac.man.model;

import java.util.Map;

import android.graphics.Point;
import pac.man.gfx.Animation;
import pac.man.model.Character.AnimationType;
import pac.man.util.Vector;

public class Ghost extends Character {
    private Vector target;

    public Ghost(Vector position, Map<AnimationType, Animation> animations) {
        super(new Vector(animations.values().iterator().next().getWidth(), animations.values().iterator().next()
                .getHeight()), position, animations);
    }

    public Vector getTarget() {
        return target;
    }
}
