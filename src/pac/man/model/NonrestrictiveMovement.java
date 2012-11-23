package pac.man.model;

import pac.man.util.Vector;

public class NonrestrictiveMovement extends MovementAlgorithm {
    public Vector computeSpeed(Vector position, Vector currentSpeed, Vector preferredDir) {
        // PreferredDirection ought to be normalized here.
        preferredDir.scale(MovementAlgorithm.SPEED_GAIN);

        // Just return the direction.
        return preferredDir;
    }
}