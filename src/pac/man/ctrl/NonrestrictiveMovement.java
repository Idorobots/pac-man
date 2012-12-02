package pac.man.ctrl;

import pac.man.util.Vector;

public class NonrestrictiveMovement extends MovementAlgorithm {
    private double factor = 1.0;

    public NonrestrictiveMovement(double factor) {
        this.factor = factor;
    }

    public NonrestrictiveMovement() {
        this(1.0);
    }

    public Vector computeSpeed(Vector position, Vector currentSpeed, Vector preferredDir) {
        // PreferredDirection ought to be normalized here.
        preferredDir.scale(factor * MovementAlgorithm.getSpeed().getGain());

        // Just return the direction.
        return preferredDir;
    }
}