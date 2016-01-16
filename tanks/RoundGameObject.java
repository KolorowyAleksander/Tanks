package tanks;

import com.sun.javafx.collections.MappingChange;
import com.sun.javafx.iio.ImageLoader;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.Map;

public abstract class RoundGameObject {
    protected Point2D center;
    protected double rotationAngle;
    public double velocity;
    public double angularVelocity;
    protected Circle collisionBounds;
    protected Image image;

    public RoundGameObject(double startX, double startY, double startDegree, double  radius, double velocity, double angularVelocity) {
        center = new Point2D(startX, startY);
        this.rotationAngle = startDegree;
        collisionBounds = new Circle(startX, startY, radius);
        this.velocity = velocity;
        this.angularVelocity = angularVelocity;
    }

    public double getCenterX() {
        return center.getX();
    }

    public double getCenterY() {
        return center.getY();
    }

    public Point2D getCenterPoint() {
        return center;
    }

    public double getRotationAngle() {return rotationAngle;}

    public Image getImage() {return image;}

    public double getRadius() {return collisionBounds.getRadius();}


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
        if (direction == Move.Movement.Staying) {
            return;
        }

        double angleInRadians = Math.toRadians(rotationAngle);

        double dx = velocity * deltaTime * Math.sin(angleInRadians);
        double dy = -velocity * deltaTime * Math.cos(angleInRadians);
        if (direction == Move.Movement.Backward) {
            dx = -dx;
            dy = -dy;
        }
        center = center.add(dx, dy);
        collisionBounds.setCenterX(getCenterX());
        collisionBounds.setCenterY(getCenterY());
    }

    public boolean isColliding(RoundGameObject otherGameObject) {
        Circle thisBounds = collisionBounds;
        Circle otherBounds = otherGameObject.collisionBounds;
        Point2D thisCenter = new Point2D(thisBounds.getCenterX(), thisBounds.getCenterY());
        Point2D otherCenter = new Point2D(otherBounds.getCenterX(), otherBounds.getCenterX());
        double dx = thisCenter.getX() - otherCenter.getX();
        double dy = thisCenter.getY() - otherCenter.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        double minimumDistance = collisionBounds.getRadius() + otherBounds.getRadius();
        if (distance <= minimumDistance)
            return true;
        else
            return false;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    abstract public void update(double deltaTime);

}
