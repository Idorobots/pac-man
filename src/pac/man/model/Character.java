package pac.man.model;

import java.util.Map;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Rect;

import pac.man.util.Vector;
import pac.man.gfx.Animation;

public class Character {

    public static enum AnimationType {
        IDLE, RIGHT, UP, LEFT, DOWN, DEATH
    }

    protected Rect boundingRect;
    protected Vector size;
    protected Vector position;
    protected Vector speed = new Vector(0, 0);

    protected boolean alive = true;

    private Map<AnimationType, Animation> animations;
    private AnimationType currentAnimation = AnimationType.IDLE;

    public Character(Vector size, Vector position, Map<AnimationType, Animation> animations) {
        assert position != null;
        assert animations != null;

        this.position = position;
        this.animations = animations;
        this.size = size;

        this.boundingRect = new Rect((int) position.x, (int) position.y, (int) (position.x + size.x),
                (int) (position.y + size.y));
    }

    public void draw(Canvas canvas) {
        animations.get(currentAnimation).draw(boundingRect, canvas);
    }

    public void update(long dt, Canvas canvas) {
        int canvasW = canvas.getWidth();
        int canvasH = canvas.getHeight();

        animations.get(currentAnimation).update(dt);

        position.x += speed.x * dt / 1000.0;

        if (position.x < -size.x) {
            position.x = canvasW;
        } else if (position.x > canvasW) {
            position.x = -size.x;
        }

        position.y += speed.y * dt / 1000.0;

        if (position.y < -size.y) {
            position.y = canvasH;
        } else if (position.y > canvasH) {
            position.y = -size.y;
        }

        boundingRect.left = (int) position.x;
        boundingRect.right = (int) (position.x + size.x);
        boundingRect.top = (int) (position.y);
        boundingRect.bottom = (int) (position.y + size.y);
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector pos) {
        this.position.x = pos.x;
        this.position.y = pos.y;
    }

    public Vector getSpeed() {
        return speed;
    }

    public void setSpeed(Vector speed) {
        this.speed.x = speed.x;
        this.speed.y = speed.y;
    }

    public Vector getSize() {
        return size;
    }

    public void setSize(Vector size) {
        this.size.x = size.x;
        this.size.y = size.y;
    }

    public boolean isMoving() {
        return speed.length() >= 1.0;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean state) {
        alive = state;
    }

    public void setActiveAnimation(AnimationType index) {
        if (currentAnimation != index) {
            currentAnimation = index;
            animations.get(index).reset();
        }
    }

    public Rect getBoundingRect() {
        return boundingRect;
    }
}
