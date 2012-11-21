package pac.man.model.movement;

import pac.man.model.Board;
import pac.man.model.Ghost;

public interface IMovementAlgorithm {
    void nestStep(Ghost g, Board b);
}
