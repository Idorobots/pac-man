package pac.man.util;

public class Vector {
    public static final Vector ZERO = new Vector(0.0, 0.0);

    public double x;
    public double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector add(Vector another) {
        // NOTE Makes a copy.
        return new Vector(x + another.x, y + another.y);
    }

    public Vector scale(double k) {
        x = x * k;
        y = y * k;

        return this;
    }
}
