package pac.man.ctrl;

import pac.man.model.Player;
import pac.man.util.Vector;
import pac.man.ctrl.RandomStrategy;

public class SimpleChaseStrategy extends RandomStrategy {
    private Player player;
    private double range;

    public SimpleChaseStrategy(Player player, double range) {
        super();

        assert player != null;
        assert range > 0.0;

        this.player = player;
        this.range = range;
    }

    public Vector computeDirection(Vector position, Vector currentSpeed) {
        Vector ppos = player.getPosition();

        if(position.distanceTo(ppos) <= range) {
            return new Vector(ppos.x - position.x, ppos.y - position.y).normalize();
        }
        else {
            return super.computeDirection(position, currentSpeed);
        }
    }
}