package pac.man.ctrl;

import pac.man.model.Player;
import pac.man.util.Vector;
import pac.man.ctrl.RandomStrategy;

public class SimpleChaseStrategy extends RandomStrategy {
    private Player player;
    private double range;
    private double factor;

    public SimpleChaseStrategy(Player player, double range, double factor) {
        super();

        assert player != null;
        assert range > 0.0;

        this.player = player;
        this.range = range;
        this.factor = factor;
    }

    public Vector computeDirection(Vector position, Vector currentSpeed) {
        Vector ppos = player.getPosition();

        if(position.distanceTo(ppos) <= range) {
            return new Vector(ppos.x - position.x, ppos.y - position.y).normalize().scale(factor);
        }
        else {
            return super.computeDirection(position, currentSpeed);
        }
    }
}