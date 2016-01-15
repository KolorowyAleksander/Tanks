package tanks;

public abstract class Player {
    private Tank tank;
    private int playerNumber;

    abstract public Move makeMove(Move.Movement movement, Move.Rotation rotation, Move.Shooting shooting);

    public int getPlayerNumber() {
        return playerNumber;
    }
}
