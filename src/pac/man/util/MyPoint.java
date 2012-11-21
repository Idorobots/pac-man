package pac.man.util;

import android.graphics.Point;

public class MyPoint extends Point {

    public MyPoint() {
        super();
    }

    public MyPoint(Point p) {
        super(p);
    }

    public MyPoint(int x, int y) {
        super(x, y);
    }

    public MyPoint add(Point p) {
        return new MyPoint(x + p.x, y + p.y);
    }

    public MyPoint mul(int k) {
        return new MyPoint(x * k, y * k);
    }
    
    public double dist(Point p) {
        return Math.sqrt((p.x - x)*(p.x - x) + (p.y - y)*(p.y - y));
    }
}
