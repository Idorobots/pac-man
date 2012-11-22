package pac.man.util;

public class Vector {
    public static final Vector ZERO = new Vector(0.0, 0.0);

    public double x;
    public double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void add(Vector another) {
        x += another.x;
        y += another.y;
    }

    public void scale(double k) {
        x = x * k;
        y = y * k;
    }

    public double length() {
        return Math.sqrt(x*x + y*y);
    }

    public void normalize() {
        double len = length();
        x /= len;
        y /= len;
    }
}
