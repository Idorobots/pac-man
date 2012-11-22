package pac.man.model;

import android.graphics.Point;
import pac.man.util.Vector;
import pac.man.gfx.Animation;
import pac.man.model.Character;

public class Player extends Character {

    public Player(Point position, Animation[] animations) {
        super(new Point(animations[0].getWidth(), animations[0].getHeight()), position, animations);
    }

    public void handleMove(int eventX, int eventY, int canvasW, int canvasH) {
        // FIXME Should be handled by GamePanel, maby?
        int EDGE_WIDTH = 50;
        int SPEED_GAIN = 100;

        // FIXME Wat do when x and y are in the corner?
        if(eventY < EDGE_WIDTH) {
            speed.x = 0;
            speed.y = -SPEED_GAIN;
        }
        else if(eventY > canvasH - EDGE_WIDTH) {
            speed.x = 0;
            speed.y = SPEED_GAIN;
        }
        else if(eventX < EDGE_WIDTH) {
            speed.y = 0;
            speed.x = -SPEED_GAIN;
        }
        else if(eventX > canvasW - EDGE_WIDTH) {
            speed.y = 0;
            speed.x = SPEED_GAIN;
        }
    }

    public void update(long dt, int canvasW, int canvasH) {
        super.update(dt, canvasW, canvasH);

        if(isMoving()) {
            setActiveAnimation(1);
        }
        else setActiveAnimation(0);
    }
}