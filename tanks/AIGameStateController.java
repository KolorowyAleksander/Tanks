package tanks;

import javafx.geometry.Point2D;

public class AIGameStateController extends GameStateController{
    protected void drawVisionLines(Tank[] tanks) {
        for(Tank tank : tanks) {
            double visionAngle = tank.getRangeOfVision();
            double radius = tank.getRadius();
            double rotationAngle = tank.getRotationAngle();
            double centerX = tank.getCenterX();
            double centerY = tank.getCenterY();
            double alfaOne = Math.toRadians(rotationAngle + visionAngle / 2);
            double alfaTwo = Math.toRadians(rotationAngle - visionAngle / 2);

            Point2D pointOne = createPointDistantRadius(centerX, centerY, radius, alfaOne);
            Point2D pointTwo = createPointDistantRadius(centerX, centerY, radius, alfaTwo);
        }
    }

    private Point2D createPointDistantRadius(double centerX, double centerY, double radius, double angle) {
        return new Point2D(centerX + radius * Math.cos(angle), centerY + radius * Math.sin(angle));
    }
}
