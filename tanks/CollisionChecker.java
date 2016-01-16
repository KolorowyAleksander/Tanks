package tanks;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

public class CollisionChecker {
    private double boundariesWidth;
    private double boundariesHeight;

    public CollisionChecker(double width, double height) {
        this.boundariesWidth = width;
        this.boundariesHeight = height;
    }

    public boolean isObjectOutsideOfBoundaries(RoundGameObject gameObject) {
        if (isCircleInsideOfBoundaries(new Circle(gameObject.getCenterX(), gameObject.getCenterY(), gameObject.getRadius()))) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean checkWhetherMoveIsPossible(RoundGameObject gameObject, double deltaTime, Move.Movement direction) {
        Point2D positionDelta = gameObject.deltaOfMovement(gameObject.getVelocity(), gameObject.getRotationAngle(), deltaTime, direction);
        Point2D newPosition = gameObject.getCenterPoint().add(positionDelta);

        if(isCircleInsideOfBoundaries(new Circle(newPosition.getX(), newPosition.getY(), gameObject.getRadius()))) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isCircleInsideOfBoundaries(Circle circle) {
        if ((circle.getCenterX() - circle.getRadius()) < 0) {
            return false;
        }
        else if ((circle.getCenterX() + circle.getRadius()) > boundariesWidth) {
            return false;
        }
        else if ((circle.getCenterY() - circle.getRadius()) < 0) {
            return false;
        }
        else if ((circle.getCenterY() + circle.getRadius()) > boundariesHeight) {
            return false;
        }
        else {
            return true;
        }
    }


    public boolean isColliding(RoundGameObject objectA, RoundGameObject objectB) {
        Circle boundsA = objectA.getCollisionBounds();
        Circle boundsB = objectB.getCollisionBounds();
        Point2D centerA = new Point2D(boundsA.getCenterX(), boundsA.getCenterY());
        Point2D centerB = new Point2D(boundsB.getCenterX(), boundsB.getCenterY());
        double dx = centerA.getX() - centerB.getX();
        double dy = centerA.getY() - centerB.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        double minimumDistance = boundsA.getRadius() + boundsB.getRadius();
        if (distance <= minimumDistance)
            return true;
        else
            return false;
    }
}
