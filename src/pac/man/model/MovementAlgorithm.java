package pac.man.model;

import pac.man.util.Vector;

public abstract class MovementAlgorithm {
    public static final int SPEED_GAIN = 100;

    public abstract Vector computeSpeed(Vector position, Vector currentSpeed, Vector prefeedDirection);
}