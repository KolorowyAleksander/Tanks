package tanks;

public interface IPlayer {
    Move makeMove(Move.Movement movement, Move.Rotation rotation, boolean isShooting);
}
