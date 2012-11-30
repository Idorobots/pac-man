package pac.man.ctrl;

import pac.man.util.Vector;

public class NonrestrictiveMovement extends MovementAlgorithm {
    public Vector computeSpeed(Vector position, Vector currentSpeed, Vector preferredDir) {
        // PreferredDirection ought to be normalized here.
        preferredDir.scale(MovementAlgorithm.getSpeed().getGain());

        // Just return the direction.
        return preferredDir;
    }
}