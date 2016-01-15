package tanks;

public interface Player {
    Move makeMove(Move.Movement movement, Move.Rotation rotation, boolean isShooting);
}
