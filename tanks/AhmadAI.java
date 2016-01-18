package tanks;

import javafx.geometry.Point2D;

public class AhmadAI extends ArtificialPlayer{
    enum Action { NotOnPosition, OnPosition};
    Action currentAction = Action.NotOnPosition;

    public AhmadAI(int playerNumber, String playerName, Tank tank) {
        super(playerNumber, playerName, tank);
    }

    private boolean isInCentrum() {
        Point2D center = getPlayerTank().getCenterPoint();
        double deltaX = Math.abs(center.getX() - 400);
        double deltaY = Math.abs(center.getY() - 300);
        if (deltaX < 20 && deltaY < 20) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isOnWayToCenter() {
        Point2D fieldCenter = new Point2D(400, 300);
        double x = getPlayerTank().getCenterX(), y = getPlayerTank().getCenterY();
        double angle = Math.toDegrees(Math.atan2(y - fieldCenter.getY(), x - fieldCenter.getX()));
        return true;
    }

    public Move makeMove(double deltaTime) {

        return new Move();
    }
}
