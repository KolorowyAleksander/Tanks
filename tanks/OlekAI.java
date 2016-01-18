package tanks;

public class OlekAI extends ArtificialPlayer {

    private double myX;
    private double myY;
    private double enemyX;
    private double enemyY;

    public OlekAI(int playerNumber, Tank tank) {
        super(playerNumber, "BAWS", tank);
    }

    private boolean seeTank() {
        for (RoundGameObject element : visibleObjectsBuffer) {
            if (element instanceof Tank) {
                enemyX = element.getCenterX();
                enemyY = element.getCenterY();
                return true;
            }
        }
        return false;
    }

    private double countAlpha(double enemyX, double enemyY) {
        double alpha = Math.toDegrees(Math.atan2(enemyY - myY, enemyX - myX));
        alpha -= 180;
        if (alpha < 0) {
            alpha += 360;
        }
        return alpha;
    }

    private Move.Rotation rotate(double alpha) {
        Move.Rotation rotation = Move.Rotation.Staying;
        if (alpha >= this.getPlayerTank().getRotationAngle())
            rotation = Move.Rotation.CounterClockwise;
        if (alpha < this.getPlayerTank().getRotationAngle())
            rotation = Move.Rotation.Clockwise;
        return rotation;
    }

    public Move makeMove(double deltaTime) {
        double alpha;
        myX = this.getPlayerTank().getCenterX();
        myY = this.getPlayerTank().getCenterY();
        Move.Rotation currentRotation;
        if (seeTank()) {
            alpha = countAlpha(enemyX, enemyY);
            currentRotation = rotate(alpha);
        } else {
            currentRotation = Move.Rotation.CounterClockwise;
        }
        return new Move(Move.Movement.Forward, currentRotation, Move.Shooting.Shoots);
    }
}
