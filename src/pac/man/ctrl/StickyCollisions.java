package pac.man.ctrl;

import android.graphics.Rect;
import android.graphics.Canvas;
import pac.man.util.Vector;
import pac.man.model.Character;

public class StickyCollisions extends CollisionHandler {
    public void handle(long dt, Canvas canvas, Rect rect, Character c) {
        Vector speed = c.getSpeed();
        speed.scale(-1.0);

        c.setSpeed(speed);
        Rect b;
        do {
            c.update(dt, canvas); // reverse for a while
            b = c.getBoundingRect();
        } while(Rect.intersects(b, rect));

        c.setSpeed(new Vector(0, 0));
    }
}