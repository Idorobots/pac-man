package pac.man.ctrl;

import pac.man.util.Vector;

public abstract class MovementStrategy {
    public abstract Vector computeDirection(Vector position, Vector currentSpeed);
}