package pac.man.model;

import java.util.Map;

import android.graphics.Canvas;
import android.graphics.Point;

import pac.man.util.Vector;
import pac.man.util.Animation;
import pac.man.model.Character;
import pac.man.model.Character.AnimationType;
import pac.man.ctrl.NonrestrictiveMovement;

public class Player extends Character {
    public Player(Vector position, Map<AnimationType, Animation> animations) {
        super(new Vector(animations.values().iterator().next().getWidth(),
                         animations.values().iterator().next().getHeight()),
              position, animations);

        assert animations.keySet().size() >= 6;

        // Animations:
        // - 0 - idle
        // - 1 - right
        // - 2 - up
        // - 3 - left
        // - 4 - down
        // - 5 - death

        // Defaults to nonrestrictive movement algorithm.
        movementAlgorithm = new NonrestrictiveMovement();
    }

    public void handleMove(Vector direction) {
        setSpeed(movementAlgorithm.computeSpeed(getPosition(), getSpeed(), direction));
    }
}