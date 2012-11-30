package pac.man.ctrl;

import pac.man.util.Vector;

public class Strict4WayMovement extends MovementAlgorithm {
    private Vector[] speeds;

    public Strict4WayMovement() {
        speeds = new Vector[4];

        // Speeds:
        // - 0 - right
        // - 1 - up
        // - 2 - left
        // - 3 - down

        speeds[0] = new Vector(1.0, 0.0);
        speeds[1] = new Vector(0.0, -1.0);
        speeds[2] = new Vector(-1.0, 0.0);
        speeds[3] = new Vector(0.0, 1.0);
    }

    public Vector computeSpeed(Vector position, Vector currentSpeed, Vector preferredDir) {

        // Similar to Player.handleMove() logic. It's documented there.
        long angle = 360L + Math.round(Math.toDegrees(Math.atan2(-preferredDir.y, preferredDir.x)));
        long index = ((angle + 45) % 360) / 90;

        Vector vec = new Vector(speeds[(int) index]);
        return vec.scale(MovementAlgorithm.getSpeed().getGain());
    }
}