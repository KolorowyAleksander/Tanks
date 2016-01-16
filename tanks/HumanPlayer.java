package tanks;

public class HumanPlayer extends Player {
    private Move moveBuffer;

    public HumanPlayer(int playerNumber, String playerName, Tank tank) {
        super(playerNumber, playerName, tank);
        moveBuffer = new Move();
    }

    @Override
    public Move makeMove(Move.Movement movement, Move.Rotation rotation, Move.Shooting shooting) {
        moveBuffer = new Move();
        return new Move(movement, rotation, shooting);
    }

    public void orderInput() {} ;

    public void setMoveBufferSection(Move.MoveSections section, Enum value) {
        moveBuffer.setMoveSection(section, value);
    }
}
