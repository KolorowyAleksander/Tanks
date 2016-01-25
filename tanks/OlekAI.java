package tanks;

public class OlekAI extends ArtificialPlayer {

    private Tank enemy;

    public OlekAI(int playerNumber, Tank tank) {
        super(playerNumber, "BAWS", tank);
    }

    private boolean seeTank() {
        for (RoundGameObject element : visibleObjectsBuffer) {
            if (element instanceof Tank) {
                enemy = (Tank) element;
                return true;
            }
        }
        return false;
    }

    private double countAlpha() {
        double alpha = Math.toDegrees(Math.atan2(enemy.getCenterY() - this.getPlayerTank().getCenterY(),
                enemy.getCenterX() - this.getPlayerTank().getCenterY()));

        alpha += 450;

        alpha = alpha % 360;

        return alpha;
    }

    private Move.Rotation setRotation(double alpha) {
        Move.Rotation rotation = Move.Rotation.Staying;
        double beta = this.getPlayerTank().getRotationAngle();
        if (alpha > beta)
            rotation = Move.Rotation.CounterClockwise;
        else if (alpha < beta)
            rotation = Move.Rotation.Clockwise;
        return rotation;
    }

    private Move.Rotation currentRotation;

    public Move makeMove(double deltaTime) {
        double alpha;
        if (seeTank()) {
            alpha = countAlpha();
            currentRotation = setRotation(alpha);
        } else {
            currentRotation = Move.Rotation.CounterClockwise;
        }
        return new Move(Move.Movement.Forward, currentRotation, Move.Shooting.Shoots);
    }
}
