package pac.man.model;

import android.graphics.Point;
import android.graphics.Canvas;
import android.graphics.Rect;

import pac.man.util.Vector;
import pac.man.gfx.Animation;

/** Klasa bazowa dla duszków i żółtka */
public class Character {
    protected Point size;
    protected Point position;
    protected Vector speed = new Vector(0, 0);

    boolean alive = true;

    private Animation[] animations;
    private int currentAnimation = 0;

    public Character(Point size, Point position, Animation[] animations) {
        assert position != null;
        assert animations != null;

        this.position = position;
        this.animations = animations;
        this.size = size;
    }

    public void draw(Canvas canvas) {
        animations[currentAnimation].draw(position, canvas);
    }

    public void update(long dt, int canvasW, int canvasH) {
        animations[currentAnimation].update(dt);

        position.x += (int) (speed.x * dt/1000.0);

        if(position.x < -size.x) {
            position.x = canvasW;
        }
        else if(position.x > canvasW) {
            position.x = -size.x;
        }

        position.y += (int) (speed.y * dt/1000.0);

        if(position.y < -size.y) {
            position.y = canvasH;
        }
        else if(position.y > canvasH) {
            position.y = -size.y;
        }
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point pos) {
        this.position = pos;
    }

    public Vector getSpeed() {
        return speed;
    }

    public void setSpeed(Vector speed) {
        this.speed = speed;
    }

    public boolean isMoving() {
        return Math.abs(speed.x) > 1.0 || Math.abs(speed.y) > 1.0;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean state) {
        alive = state;
    }

    public void setActiveAnimation(int index) {
        assert index < animations.length;

        if(currentAnimation != index) {
            currentAnimation = index;
            animations[index].reset();
        }
    }
}
