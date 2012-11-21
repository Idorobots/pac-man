package pac.man.model.movement;

import java.util.HashMap;
import java.util.Map;

import pac.man.model.Board;
import pac.man.model.Board.Type;
import pac.man.model.Ghost;
import pac.man.model.MyPoint;
import android.graphics.Point;

public class DefaultMovement implements IMovementAlgorithm {

    @Override
    public void nestStep(Ghost g, Board b) {
        // TODO Auto-generated method stub

        /*
         * Sprawdzić dostępne płytki przyległe do płytki przed duszkiem.
         * Wybrany kierunek: najkrótsza droga do celu. 
         */
        
        // Punkt przed duszkiem.
        MyPoint frontTile = new MyPoint(g.getLocation()).add(g.getDirection().getCoords());
        
        double shortestPath = Double.MAX_VALUE;
        Map<Point, Direction> availableChoices = new HashMap<Point, Direction>();
        for (Direction d : Direction.values()) {
            MyPoint dest = frontTile.add(d.getCoords());
            if (b.getCell(dest) == Type.PASSABLE && !g.getOccupiedTile().equals(dest)) {
                availableChoices.put(dest, d);
                shortestPath = Math.min(shortestPath, dest.dist(g.getTarget()));
            }
        }
        
        // Usuń dłuższe ścieżki.
        for (Point p : availableChoices.keySet()) {
            MyPoint dest = new MyPoint(g.getTarget());
            if (dest.dist(p) > shortestPath)
                availableChoices.remove(p);
        }
        
        
    }

}
