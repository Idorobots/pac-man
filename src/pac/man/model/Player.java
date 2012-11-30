package pac.man.model;

import java.util.Map;

import android.graphics.Canvas;
import android.graphics.Point;

import pac.man.util.Vector;
import pac.man.gfx.Animation;
import pac.man.model.Character;
import pac.man.model.MovementAlgorithm;
import pac.man.model.Character.AnimationType;

public class Player extends Character {

    private MovementAlgorithm movementAlgorithm;

    public Player(Vector position, Map<AnimationType, Animation> animations) {
        super(new Vector(animations.values().iterator().next().getWidth(),
                animations.values().iterator().next().getHeight()), position, animations);

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
        // 1. Fancy vector magic:

        setSpeed(movementAlgorithm.computeSpeed(getPosition(), getSpeed(), direction));

        // 2. Setting correct animation:
        //
        // Since the coordinate system looks like this:
        //
        // +----------------+-----------> X
        // |                |
        // |                |
        // |                |
        // +--------------(ಠ_ಠ) <--- PacMan
        // |
        // v
        // Y
        //
        // ...we need to reverse the y coordinate when computing the angle.
        // We also add 360 degrees to eliminate negative values not to produce IndexOutOfBoundsException.

        long angle = 360L + Math.round(Math.toDegrees(Math.atan2(-direction.y, direction.x)));

        // Now our direction field looks like this:
        //
        //         |
        //         |   ^
        //    <-   |   |
        //         |
        //  -------+--------
        //         |
        //     |   |   ->
        //     v   |
        //         |
        //
        // ...but we want it to look like this:
        //
        //    \   ^   /
        //     \  |  /
        //      \   /
        //       \ /
        //  <-    X    ->
        //       / \
        //      /   \
        //     /  |  \
        //    /   v   \
        //
        // ...so we add extra 45 degrees to the angle rotating the coordinate system.

        int index = 1 + (int) (((angle + 45) % 360) / 90);
        
        // XXX: niezbyt ładne, ale skuteczne ; )
        AnimationType animationType = null;
        switch (index) {
            case 1: animationType = AnimationType.RIGHT; break;
            case 2: animationType = AnimationType.UP; break;
            case 3: animationType = AnimationType.LEFT; break;
            case 4: animationType = AnimationType.DOWN; break;
        }
        setActiveAnimation(animationType);
    }

    public void update(long dt, Canvas canvas) {
        super.update(dt, canvas);

        if(!isAlive()) {
            setActiveAnimation(AnimationType.DEATH);
        }
        else if(!isMoving()) {
            setActiveAnimation(AnimationType.IDLE);
        }
    }

    public MovementAlgorithm getMovementAlgorithm() {
        return movementAlgorithm;
    }

    public void setMovementAlgorithm(MovementAlgorithm a) {
        movementAlgorithm = a;
    }

}