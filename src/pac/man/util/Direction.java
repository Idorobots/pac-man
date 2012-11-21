package pac.man.util;

import pac.man.util.MyPoint;

public enum Direction {
    UP(new MyPoint(0, -1)), RIGHT(new MyPoint(1, 0)), DOWN(new MyPoint(0, 1)), LEFT(new MyPoint(-1, 0));

    private final MyPoint coords;


    private Direction(MyPoint coords) {
        this.coords = new MyPoint(coords);
    }


    public MyPoint getCoords() {
        return coords;
    }
}
