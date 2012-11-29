package pac.man.util;

public class Vector {
    public static final Vector ZERO = new Vector(0.0, 0.0);

    public double x;
    public double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Vector vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    public void add(Vector another) {
        x += another.x;
        y += another.y;
    }

    public Vector scale(double k) {
        x = x * k;
        y = y * k;
        
        return this;
    }

    public double length() {
        return Math.sqrt(x*x + y*y);
    }

    public Vector normalize() {
        double len = length();
        x /= len;
        y /= len;
        
        return this;
    }
}
