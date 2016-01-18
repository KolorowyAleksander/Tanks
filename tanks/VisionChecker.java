package tanks;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

public class VisionChecker extends Checker{
    public VisionChecker(double width, double height) {
        super(width, height);
    }

    public boolean isObjectVisible(RoundGameObject prospector, double rangeOfVision, RoundGameObject otherObject) {
        double trueRotationAngle = ((prospector.getRotationAngle() - 90) + 360) % 360;

        Point2D centerOfProspector = prospector.getCenterPoint();
        Point2D centerOfOtherObject = otherObject.getCenterPoint();

        if(isPointInRangeOfVision(centerOfProspector, trueRotationAngle, rangeOfVision, centerOfOtherObject)) {
            return true;
        }

        double a = (otherObject.getCenterY() - prospector.getCenterY()) / (otherObject.getCenterX() - prospector.getCenterX());
        double b = a * otherObject.getCenterX() - otherObject.getCenterY();

        /*if(isLinearFunctionCrossingCircle(a, b, otherObject.getCollisionBounds())) {
            return true;
        }*/

        return false;
    }

    protected boolean isPointInRangeOfVision(Point2D prospector, double rotationAngle, double rangeOfVision, Point2D otherObject) {
        double alfa = Math.toDegrees(Math.atan2((otherObject.getY() - prospector.getY()), (otherObject.getX() - prospector.getX())));
        double lowerBoundaries = ((rotationAngle - rangeOfVision / 2) + 360) % 360;
        double higherBoundaries = ((rotationAngle + rangeOfVision / 2) + 360) % 360;
        if ((alfa >= lowerBoundaries) && (alfa <= higherBoundaries)) {
            return true;
        }
        else {
            return false;
        }
    }

    protected boolean isLinearFunctionCrossingCircle(double aCoefficient, double bCoefficient, Circle circle) {
        double x = circle.getCenterX(), y = circle.getCenterY(), radius = circle.getRadius();
        double distance = (Math.abs(aCoefficient * x + y + bCoefficient)) / Math.sqrt(aCoefficient * aCoefficient + 1);

        if (distance < radius) {
            return true;
        }
        else {
            return false;
        }
    }
}
