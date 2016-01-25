package tanks;

import javafx.geometry.Point2D;

import java.time.Clock;
import java.util.*;
import java.util.List;

public class KubaAI extends ArtificialPlayer {
    public KubaAI (int playerNumber, Tank tank) {
        super(playerNumber, "Nuurek", tank);
        currentAction = Action.Searching;
        lastPosition = getPlayerTank().getCenterPoint();
        target = null;
    }

    public Move makeMove(double deltaTime) {
        Point2D tankPosition = getPlayerTank().getCenterPoint();
        switch (currentAction) {
            case Searching:
                if (target == null) {
                    searchForTarget();
                }
                if (target != null) {
                    currentAction = Action.ReachingTarget;
                }
                break;

            case ReachingTarget:
                if ((euclideanSpaceDistance(target, getPlayerTank().getCenterPoint()) < bonusRadius)) {
                    target = null;
                    isAngleSet = false;
                }
                if (target == null) {
                    currentAction = Action.Searching;
                    isSearchingStarted = false;
                }
                break;
        }

        switch (currentAction) {
            case Idle:
                break;

            case Searching:
                if (!isSearchingStarted) {
                    rotationToDo = getBestRotationDirection();
                    isSearchingStarted = true;
                }
                movementToDo = Move.Movement.Staying;
                break;

            case ReachingTarget:
                double bonusAngle = (Math.toDegrees(Math.atan2(target.getY() - tankPosition.getY(), target.getX() - tankPosition.getX())) + 360) % 360;
                double tankRotationAngle = (getPlayerTank().getRotationAngle() + 270) % 360;
                double bonusVisionAngle = (Math.toDegrees(Math.atan2(bonusRadius,
                        euclideanSpaceDistance(tankPosition, target))) + 360) % 360;
                double deltaAngle = (bonusAngle - tankRotationAngle + 360) % 360;

                double shift = euclideanSpaceDistance(tankPosition, lastPosition);
                double maxShift = 2 * getPlayerTank().getVelocity() * (1.0 / GameState.getFramesPerSecond());
                if (shift > maxShift) {
                    isAngleSet = false;
                }

                if (!isAngleSet) {
                    if (deltaAngle < 180 + bonusVisionAngle && deltaAngle > 180 - bonusVisionAngle) {
                        isAngleSet = true;
                    }
                    else if (deltaAngle >= 0 && deltaAngle <= 180 - bonusVisionAngle){
                        rotationToDo = Move.Rotation.CounterClockwise;
                    }
                    else if (deltaAngle >= 180 + bonusVisionAngle && deltaAngle < 360) {
                        rotationToDo = Move.Rotation.Clockwise;
                    }
                    movementToDo = Move.Movement.Staying;
                }
                else {
                    double tankRadius = getPlayerTank().getRadius();
                    if (tankPosition.getY() - tankRadius <= tankRadius / 8) {
                        if (tankRotationAngle >= 0 && tankRotationAngle < 90) {
                            rotationToDo = Move.Rotation.CounterClockwise;
                        }
                        else if (tankRotationAngle > 90 && tankRotationAngle <= 180) {
                            rotationToDo = Move.Rotation.Clockwise;
                        }
                    }
                    else if (tankPosition.getX() + tankRadius >= GameState.getFieldWidth() - tankRadius / 8) {
                        if (tankRotationAngle >= 90 && tankRotationAngle < 180) {
                            rotationToDo = Move.Rotation.CounterClockwise;
                        }
                        else if (tankRotationAngle > 180 && tankRotationAngle <= 270) {
                            rotationToDo = Move.Rotation.Clockwise;
                        }
                    }
                    else if (tankPosition.getY() + tankRadius >= GameState.fieldHeight - tankRadius / 8) {
                        if (tankRotationAngle >= 180 && tankRotationAngle < 270) {
                            rotationToDo = Move.Rotation.CounterClockwise;
                        }
                        else if (tankRotationAngle > 270 && tankRotationAngle <= 360) {
                            rotationToDo = Move.Rotation.Clockwise;
                        }
                    }
                    else if (tankPosition.getX() - tankRadius <= tankRadius / 8) {
                        if (tankRotationAngle >= 270 && tankRotationAngle < 360) {
                            rotationToDo = Move.Rotation.CounterClockwise;
                        }
                        else if (tankRotationAngle > 0 && tankRotationAngle <= 90) {
                            rotationToDo = Move.Rotation.Clockwise;
                        }
                    }
                    else {
                        rotationToDo = Move.Rotation.Staying;
                    }
                    if (rotationToDo != Move.Rotation.Staying){
                        isAngleSet = false;
                    }
                    movementToDo = Move.Movement.Backward;
                }

                lastPosition = getPlayerTank().getCenterPoint();

                break;
        }

        return new Move(movementToDo, rotationToDo, shootingToDo);
    }

    Move.Movement movementToDo;
    Move.Rotation rotationToDo;
    Move.Shooting shootingToDo = Move.Shooting.Shoots;

    enum Action { Idle, Searching, ReachingTarget };
    Action currentAction = Action.Idle;
    Move.Rotation searchDirection = Move.Rotation.Staying;

    boolean isSearchingStarted = false;
    Point2D target;
    final static double bonusRadius = 24;

    boolean isAngleSet;
    Point2D lastPosition;

    private void swap(Object objectA, Object objectB) {
        Object temp = objectA;
        objectA = objectB;
        objectB = temp;
    }

    private void searchForTarget() {
        Point2D closestBonus = null;
        double longestPath = 0;
        double shortestHealPath = Double.POSITIVE_INFINITY;
        Point2D closestHealth = null;
        for (RoundGameObject object : visibleObjectsBuffer) {
            if (object instanceof Bonus) {
                double path = euclideanSpaceDistance(getPlayerTank().getCenterPoint(), object.getCenterPoint());
                if (getPlayerTank().getHealthPoints() < 700 && ((Bonus) object).getBonusType() == BonusType.HEALTH) {
                    if (shortestHealPath < path) {
                        closestHealth = new Point2D(object.getCenterX(), object.getCenterY());
                        break;
                    }
                }
                if (path > longestPath) {
                    longestPath = path;
                    closestBonus = new Point2D(object.getCenterX(), object.getCenterY());
                }
            }
        }
        if (closestHealth != null) {
            target = closestHealth;
        }
        else {
            target = closestBonus;
        }
    }

    private boolean checkWhetherTheTargetExists() {
        for (RoundGameObject object : visibleObjectsBuffer) {
            if (euclideanSpaceDistance(target, object.getCenterPoint()) < 0.1) {
                return true;
            }
        }
        return false;
    }

    private Move.Rotation getBestRotationDirection() {
        Point2D centerPosition = getPlayerTank().getCenterPoint();
        double rotationAngle = (getPlayerTank().getRotationAngle() + 270) % 360;
        double fieldWidth = GameState.getFieldWidth(), fieldHeight = GameState.getFieldHeight();

        Point2D crossPoints[] = new Point2D[2];
        GameState.SystemSides targetSides[] = new GameState.SystemSides[2];
        targetSides[0] = findSystemSide(centerPosition, rotationAngle, fieldWidth, fieldHeight);
        targetSides[1] = findSystemSide(centerPosition, (rotationAngle + 180) % 360, fieldWidth, fieldHeight);

        for (int i = 0; i < 2; i++) {
            crossPoints[i] = getCrossPointWithSide(centerPosition, rotationAngle, fieldWidth, fieldHeight, targetSides[i]);
        }

        return getShortestRotation(fieldWidth, fieldHeight, crossPoints);
    }

    private Move.Rotation getShortestRotation(final double fieldWidth, final double fieldHeight, Point2D[] crossPoints) {
        LinkedList<Point2D> path = createSortedPath(fieldWidth, fieldHeight, crossPoints);

        int index = path.indexOf(crossPoints[0]);
        int nextIndex = (index + 1) % path.size();
        double clockwiseDistance = 0;

        while(path.get(index) != crossPoints[1]) {
            clockwiseDistance += euclideanSpaceDistance(path.get(index), path.get(nextIndex));
            index = nextIndex;
            nextIndex = (nextIndex + 1) % path.size();
        }

        double counterClockwiseDistance = 2 * fieldWidth + 2 * fieldHeight - clockwiseDistance;

        if (clockwiseDistance > counterClockwiseDistance) {
            return Move.Rotation.Clockwise;
        }
        else {
            return Move.Rotation.CounterClockwise;
        }
    }

    private double euclideanSpaceDistance(Point2D start, Point2D end) {
        double deltaX = start.getX() - end.getX();
        double deltaY = start.getY() - end.getY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    private LinkedList<Point2D> createSortedPath(final double fieldWidth, final double fieldHeight, Point2D[] crossPoints) {
        LinkedList<Point2D> path = new LinkedList<Point2D>();
        path.add(new Point2D(0, 0));
        path.add(new Point2D(fieldWidth, 0));
        path.add(new Point2D(fieldWidth, fieldHeight));
        path.add(new Point2D(0, fieldHeight));
        path.add(crossPoints[0]);
        path.add(crossPoints[1]);

        Collections.sort(path, new Comparator<Point2D>() {
            @Override
            public int compare(Point2D o1, Point2D o2) {
                if (o1.getY() == 0 && o2.getY() == 0) {
                    return (o2.getX() < o1.getX()) ? 1 : -1;
                }
                else if (o1.getX() == fieldWidth && o2.getX() == fieldWidth) {
                    return (o2.getY() < o1.getY()) ? 1 : -1;
                }
                else if (o1.getY() == fieldHeight && o2.getY() == fieldHeight) {
                    return (o2.getX() > o1.getX()) ? 1 : -1;
                }
                else if (o1.getX() == 0 && o2.getX() == 0) {
                    return (o2.getY() > o1.getY()) ? 1 : -1;
                }
                else {
                    return -1;
                }
            }
        });

        return path;
    }

    protected Point2D getCrossPointWithSide(Point2D point, double rotationAngleInDegrees, double fieldWidth, double fieldHeight, GameState.SystemSides side) {
        double centerX = point.getX(), centerY = point.getY();

        double angle = (rotationAngleInDegrees + 360) % 360;

        if (angle == 90) {
            return new Point2D(centerX, fieldHeight);
        } else if (angle == 270) {
            return new Point2D(centerX, 0);
        }

        double a = Math.tan(Math.toRadians(angle));
        double b = centerY - a * centerX;

        switch (side) {
            case Right:
                return new Point2D(fieldWidth, a * fieldWidth + b);
            case Down:
                return new Point2D((fieldHeight - b) / a, fieldHeight);
            case Left:
                return new Point2D(0, b);
            case Up:
                return new Point2D(-b / a, 0);
            default:
                return new Point2D(0, 0);
        }
    }

    protected GameState.SystemSides findSystemSide(Point2D point, double rotationAngleInDegrees, double fieldWidth, double fieldHeight) {
        double centerX = point.getX(), centerY = point.getY();
        double angle = (rotationAngleInDegrees + 360) % 360;

        double angleLimits[] = new double[4];
        angleLimits[0] = Math.atan2(-centerY, fieldWidth - centerX);
        angleLimits[1] = Math.atan2(fieldHeight - centerY, fieldWidth - centerX);
        angleLimits[2] = Math.atan2(fieldHeight - centerY, -centerX);
        angleLimits[3] = Math.atan2(-centerY, -centerX);

        for (int i = 0; i < 4; i++) {
            angleLimits[i] = (Math.toDegrees(angleLimits[i]) + 360) % 360;
        }

        if (angle >= angleLimits[0] || angle <= angleLimits[1]) {
            return GameState.SystemSides.Right;
        } else if (angle < angleLimits[2]) {
            return GameState.SystemSides.Down;
        } else if (angle <= angleLimits[3]) {
            return GameState.SystemSides.Left;
        } else {
            return GameState.SystemSides.Up;
        }
    }

    protected GameState.SystemSides getOppositeSide(GameState.SystemSides side) {
        int numberOfValues = GameState.SystemSides.values().length;
        return GameState.SystemSides.values()[(side.ordinal() + numberOfValues / 2) % numberOfValues];
    }
}
