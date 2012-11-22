package pac.man.model;

import android.graphics.Canvas;
import android.graphics.Point;

import pac.man.util.Vector;
import pac.man.gfx.Animation;
import pac.man.model.Character;

public class Player extends Character {

    public Player(Vector position, Animation[] animations) {
        super(new Vector(animations[0].getWidth(), animations[0].getHeight()), position, animations);
    }

    public void handleMove(Vector direction) {
        // TODO Fancy vector magic.
        setSpeed(direction);
    }

    public void update(long dt, Canvas canvas) {
        super.update(dt, canvas);

        if(!isAlive()) {
            setActiveAnimation(0);
        }
        else if(isMoving()) {
            setActiveAnimation(1);
        }
        else setActiveAnimation(0);
    }
}