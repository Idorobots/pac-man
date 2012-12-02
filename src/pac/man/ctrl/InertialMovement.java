package pac.man.ctrl;

import pac.man.util.Vector;

public class InertialMovement extends MovementAlgorithm {
    private Vector speed;
    private double inertia;

    public InertialMovement(Vector speed, double inertia) {
        this.speed = new Vector(speed.x, speed.y);
        this.inertia = inertia;
    }

    public Vector computeSpeed(Vector position, Vector currentSpeed, Vector preferredDir) {
        speed.add(preferredDir.scale(inertia));
        speed.normalize();
        return speed.scale(MovementAlgorithm.getSpeed().getGain());
    }
}