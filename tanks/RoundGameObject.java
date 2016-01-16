package tanks;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;

public abstract class RoundGameObject {
    protected Point2D position;
    protected double rotationAngle;
    protected double velocity;
    protected double angularVelocity;
    protected Circle collisionBounds;
    protected Image image;

    protected Point2D previousPosition;

    public RoundGameObject(double startX, double startY, double startDegree, double  radius, double velocity, double angularVelocity) {
        position = new Point2D(startX, startY);
        this.rotationAngle = startDegree;
        collisionBounds = new Circle(startX, startY, radius);
        this.velocity = velocity;
        this.angularVelocity = angularVelocity;
    }

    public double getCenterX() {
        return position.getX();
    }

    public double getCenterY() {
        return position.getY();
    }

    public double getRadius() {return collisionBounds.getRadius();}

    public Point2D getCenterPoint() {
        return position;
    }

    public double getRotationAngle() {return rotationAngle;}

    public double getVelocity() {return velocity;}

    public double getAngularVelocity() {return angularVelocity;}

    public Circle getCollisionBounds() {return collisionBounds; }

    public Image getImage() {return image;}

    public Point2D getPreviousPosition() {return previousPosition;}

    public void rotate(Move.Rotation direction, double deltaTime) {
        switch (direction)
        {
            case Staying:
                break;

            case Clockwise:
                rotationAngle += deltaTime * angularVelocity;
                break;

            case CounterClockwise:
                rotationAngle -= deltaTime * angularVelocity;
                break;
        }
    }

    public void move(Move.Movement direction, double deltaTime) {
        previousPosition = position;

        if (direction == Move.Movement.Staying) {
            return;
        }

        Point2D shift = deltaOfMovement(velocity, rotationAngle, deltaTime, direction);

        move(shift);
    }

    public void move(Point2D shift) {
        position = position.add(shift);

        collisionBounds.setCenterX(getCenterX());
        collisionBounds.setCenterY(getCenterY());
    }

    public Point2D deltaOfMovement(double velocity, double rotationAngle, double deltaTime, Move.Movement direction) {
        double angleInRadians = Math.toRadians(rotationAngle);

        double dx = velocity * deltaTime * Math.sin(angleInRadians);
        double dy = -velocity * deltaTime * Math.cos(angleInRadians);

        Point2D shift = new Point2D(dx, dy);

        if (direction == Move.Movement.Backward){
            shift = reverseDirection(shift);
        }

        return shift;
    }

    protected Point2D reverseDirection(Point2D shift) {
        return new Point2D(-shift.getX(), -shift.getY());
    }

    public void setImage(Image image) {
        this.image = image;
    }

    abstract public void update(double deltaTime);

}
