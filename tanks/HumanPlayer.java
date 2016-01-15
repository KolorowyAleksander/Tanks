package tanks;

public class HumanPlayer extends Player {

    @Override
    public Move makeMove(Move.Movement movement, Move.Rotation rotation, boolean isShooting) {
        return new Move(movement, rotation, isShooting);
    }
}
