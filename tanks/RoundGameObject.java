package tanks;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;

public class RoundGameObject {
    protected Point2D center;
    public double rotationAngle;
    public double velocity;
    public Circle collisionBounds;
    public Image image;

    public RoundGameObject(double startX, double startY, double startDegree, double  radius, double velocity) {
        center = new Point2D(startX, startY);
        this.rotationAngle = startDegree;
        collisionBounds.setCenterX(startX);
        collisionBounds.setCenterY(startY);
        collisionBounds.setRadius(radius);
        this.velocity = velocity;
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

    public void move() {
        double dx = center.getX() + velocity * Math.sin(rotationAngle);
        double dy = center.getY() + velocity * Math.cos(rotationAngle);
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

    public void update(double deltaTime) {
        double deltaX = velocity * Math.cos(rotationAngle) * deltaTime;
        double deltaY = velocity * Math.sin(rotationAngle) * deltaTime;
        center.add(deltaX, deltaY);
    }

}
