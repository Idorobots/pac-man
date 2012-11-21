package pac.man.model;

import pac.man.model.movement.Direction;
import android.graphics.Point;

/** Klasa bazowa dla duszków i żółtka */
public class Character {
    private static final int FRAMES_PER_TILE = 4;
        
    /**
     * Punkt renderowania.
     */
    private Point location;
    
    private Direction direction;
    
    /**
     * Numer aktualnej klatki animacji.
     */
    private int animationFrame;

    /**
     * Zwraca płytkę zajętą przez postać w zależności od położenia.
     * 
     * @return
     */
    public Point getOccupiedTile() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public Point getLocation() {
        return location;
    }

    public Direction getDirection() {
        return direction;
    }

}
