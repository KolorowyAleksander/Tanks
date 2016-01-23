package tanks;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

public class VisionChecker extends Checker{
    public VisionChecker(double width, double height) {
        super(width, height);
    }

    public boolean isObjectVisible(RoundGameObject prospector, double rangeOfVision, RoundGameObject otherObject) {
        double trueRotationAngle = ((prospector.getRotationAngle() - 90) + 360) % 360;

        double visionBoundaries[] = getVisionBoundaries(trueRotationAngle, rangeOfVision);

        Point2D centerOfProspector = prospector.getCenterPoint();
        Point2D centerOfOtherObject = otherObject.getCenterPoint();

        if(isPointInRangeOfVision(centerOfProspector, visionBoundaries, centerOfOtherObject)) {
            return true;
        }

        for (double visionBoundary : visionBoundaries) {
            if (isObjectCrossingVisionBoundary(centerOfProspector, visionBoundary, otherObject)) {
                if (!(otherObject instanceof Bullet)) {
                    System.out.println("Vision boundary " + visionBoundary + " of " + prospector.getClass().toString()
                            + " has crossed " + otherObject.getClass().toString());
                }
                return true;
            }
        }

        return false;
    }

    private boolean isObjectCrossingVisionBoundary(Point2D centerOfProspector, double visionBoundary, RoundGameObject otherObject) {
        double radius = otherObject.getRadius();

        if (visionBoundary == 90) {
            if (Math.abs(centerOfProspector.getX() - otherObject.getCenterX()) <= radius) {
                if (otherObject.getCenterY() >= centerOfProspector.getY()) {
                    return true;
                } else {
                    return false;
                }
            }
        } else if (visionBoundary == 270) {
            if (Math.abs(centerOfProspector.getX() - otherObject.getCenterX()) <= radius) {
                if (otherObject.getCenterY() <= centerOfProspector.getY()) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            Point2D crossPoints[] = getObjectAndVisionBoundaryCrossPoints(centerOfProspector, visionBoundary, otherObject);
            if (crossPoints.length == 0) {
                return false;
            }
            else {
                final double deltaAnglePrecision = 5;
                double angle = (Math.toDegrees(Math.atan2(crossPoints[0].getY() - centerOfProspector.getY(),
                        crossPoints[0].getX() - centerOfProspector.getX())) + 360) % 360;
                double deltaAngle = Math.abs(visionBoundary - angle);
                if (deltaAngle < deltaAnglePrecision) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        return false;
    }

    private Point2D[] getObjectAndVisionBoundaryCrossPoints(Point2D centerOfProspector, double visionBoundary, RoundGameObject otherObject) {
        double aCoefficient = Math.tan(Math.toRadians(visionBoundary));
        double bCoefficient = centerOfProspector.getY() - aCoefficient * centerOfProspector.getX();
        Point2D[] crossPoints = getLinearFunctionAndCircleCrossPoints(aCoefficient, bCoefficient, otherObject.getCenterPoint(),
                otherObject.getRadius());
        return crossPoints;
    }

    private Point2D[] getLinearFunctionAndCircleCrossPoints(double a, double b, Point2D centerPoint, double radius) {
        double xc = centerPoint.getX();
        double yc = centerPoint.getY();

        double aCoefficient = (1 + a * a);
        double bCoefficient = 2 * (a * (b - yc) - xc);
        double cCoefficient = xc * xc + (b - yc) * (b - yc) - radius * radius;

        double delta = bCoefficient * bCoefficient - 4 * aCoefficient * cCoefficient;

        if (delta < 0) {
            return new Point2D[]{};
        }
        else if (delta == 0) {
            double x0 = -bCoefficient / (2 * aCoefficient);
            double y0 = a * x0 + b;
            return new Point2D[]{new Point2D(x0, y0)};
        }
        else {
            double x1 = (-bCoefficient - Math.sqrt(delta)) / (2 * aCoefficient);
            double y1 = a * x1 + b;
            double x2 = (-bCoefficient + Math.sqrt(delta)) / (2 * aCoefficient);
            double y2 = a * x2 + b;
            return new Point2D[] {new Point2D(x1, y1), new Point2D(x2, y2)};
        }
    }

    double[] getVisionBoundaries(double rotationAngle, double rangeOfVision) {
        double visionBoundaries[] = new double[2];
        visionBoundaries[0] = ((rotationAngle - rangeOfVision / 2) + 360) % 360;
        visionBoundaries[1] =  ((rotationAngle + rangeOfVision / 2) + 360) % 360;
        return visionBoundaries;
    }

    protected boolean isPointInRangeOfVision(Point2D prospector, double visionBoundaries[], Point2D otherObject) {
        double alfa = Math.toDegrees(Math.atan2((otherObject.getY() - prospector.getY()), (otherObject.getX() - prospector.getX())));
        alfa = (alfa + 360) % 360;
        double lowerBoundaries = visionBoundaries[0];
        double higherBoundaries = visionBoundaries[1];
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
