package tanks;

import java.util.List;

public class OlekAI extends ArtificialPlayer {

    private Move.Movement currentMovement;
    private Move.Rotation currentRotation;

    private double myX;
    private double myY;
    private double enemyX;
    private double enemyY;

    public OlekAI(int playerNumber, String playerName, Tank tank) {
        super(playerNumber, playerName, tank);
    public OlekAI (int playerNumber, Tank tank) {
        super(playerNumber, "Olek222", tank);
    }

    private boolean seeTank() {
        for (RoundGameObject element : visibleObjectsBuffer) {
            if (element instanceof Tank) {
                enemyX = element.getCenterX();
                enemyY = element.getCenterY();
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
        if (seeTank()) {
            alpha = countAlpha(enemyX, enemyY);
            //System.out.println(alpha);
            currentRotation = rotate(alpha);
        } else {
            currentRotation = Move.Rotation.CounterClockwise;
        }
        currentMovement = Move.Movement.Staying;
        return new Move(currentMovement, currentRotation, Move.Shooting.Shoots);
    }
}
