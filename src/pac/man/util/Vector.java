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

    public Vector rotate(double angle) {
        angle = Math.toRadians(angle);

        double xp = x * Math.cos(angle) - y * Math.sin(angle);
        double yp = x * Math.sin(angle) + y * Math.cos(angle);

        x = xp;
        y = yp;

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

    public double distanceTo(Vector v) {
        return Math.sqrt(distanceSquaredTo(v));
    }

    public double distanceSquaredTo(Vector v) {
        double dx = v.x - x;
        double dy = v.y - y;

        return dx*dx + dy*dy;
    }
}
