package pac.man.model;

import android.graphics.Canvas;
import android.graphics.Point;

import pac.man.util.Vector;
import pac.man.gfx.Animation;
import pac.man.model.Character;

public class Player extends Character {

    public Player(Vector position, Animation[] animations) {
        super(new Vector(animations[0].getWidth(), animations[0].getHeight()), position, animations);

        assert animations.length >= 6;

        // Animations:
        // - 0 - idle
        // - 1 - right
        // - 2 - up
        // - 3 - left
        // - 4 - down
        // - 5 - death
    }

    public void handleMove(Vector direction) {
        // TODO Fancy vector magic.
        setSpeed(direction);

        long angle = 360L + Math.round(Math.toDegrees(Math.atan2(-direction.y, direction.x))); // y is reversed
        int index = 1 + (int) (((angle + 45) % 360) / 90); // A little convinient rotation

        System.out.println(String.format("%d - %d", angle, index));

        setActiveAnimation(index);
    }

    public void update(long dt, Canvas canvas) {
        super.update(dt, canvas);

        if(!isAlive()) {
            setActiveAnimation(5);
        }
        else if(!isMoving()) {
            setActiveAnimation(0);
        }
    }
}