package tanks;

public abstract class Player {
    private Tank tank;

    abstract public Move makeMove(Move.Movement movement, Move.Rotation rotation, boolean isShooting);
}
