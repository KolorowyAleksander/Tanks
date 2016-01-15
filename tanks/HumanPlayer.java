package tanks;

public class HumanPlayer extends Player {
    private Move moveBuffer;

    @Override
    public Move makeMove(Move.Movement movement, Move.Rotation rotation, Move.Shooting shooting) {
        moveBuffer = new Move();
        return new Move(movement, rotation, shooting);
    }

    public void orderInput() {} ;

    private void setMovementInMoveBuffer(int player, Move.Movement movement) {
        moveBuffer.movement = movement;
    }

    private void setRotationInMoveBuffer(int player, Move.Rotation rotation) {
        moveBuffer.rotation = rotation;
    }

    private void setShootingInMoveBuffer(int player, Move.Shooting shooting) {
        moveBuffer.shooting = shooting;
    }
}
