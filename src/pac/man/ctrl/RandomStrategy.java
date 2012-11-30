package pac.man.ctrl;

import java.util.Random;
import pac.man.util.Vector;

public class RandomStrategy extends MovementStrategy {
    private Random r = new Random();

    public Vector computeDirection(Vector position, Vector currentSpeed) {
        double angle = r.nextDouble() * 2 * Math.PI;
        return new Vector(Math.cos(angle), Math.sin(angle));
    }
}