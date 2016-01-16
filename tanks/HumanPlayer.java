package tanks;

public class HumanPlayer extends Player {
    private Move moveBuffer;

    public HumanPlayer(int playerNumber, String playerName, Tank tank) {
        super(playerNumber, playerName, tank);
        moveBuffer = new Move();
    }

    @Override
    public Move makeMove() {
        Move move = new Move(moveBuffer.getMovement(), moveBuffer.getRotation(), moveBuffer.getShooting());
        moveBuffer = new Move();
        return move;
    }

    public void orderInput() {} ;

    public void setMoveBufferSection(Move.MoveSections section, Enum value) {
        moveBuffer.setMoveSection(section, value);
    }
}
