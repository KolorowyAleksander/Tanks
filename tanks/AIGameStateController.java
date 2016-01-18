package tanks;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class AIGameStateController extends GameStateController{


    public void drawVisionLines(Tank[] tanks) {
        for(Tank tank : tanks) {
            Point2D startsOfLines[] = new Point2D[2];
            Point2D endsOfLines[] = new Point2D[2];
            double visionAngle = tank.getRangeOfVision();
            double radius = tank.getRadius();
            double rotationAngle = tank.getRotationAngle();
            double centerX = tank.getCenterX();
            double centerY = tank.getCenterY();
            double multiplier = 0, alfa;
            GameState gameState = ((GameState)associatedState);
            double fieldWidth = gameState.fieldWidth;
            double fieldHeight = gameState.fieldHeight;
            Point2D startPoint, endPoint;
            GameState.SystemSides sides[] = new GameState.SystemSides[2];
            for (int i = 0; i < 2; i++) {
                if (i == 0) {
                    multiplier = -1;
                }
                else if(i == 1) {
                    multiplier = 1;
                }

                alfa = rotationAngle - 90 + (multiplier * visionAngle) / 2;

                startPoint = createPointDistantByRadius(centerX, centerY, radius, alfa);

                GameState.SystemSides side = ((GameState)associatedState).findSystemSide(startPoint, alfa, fieldWidth, fieldHeight);
                sides[i] = side;
                endPoint = ((GameState)associatedState).getCrossPointWithSide(startPoint, alfa, fieldWidth, fieldHeight, side);
                startsOfLines[i] = startPoint;
                endsOfLines[i] = endPoint;
            }

            GraphicsContext graphicsContext = gameCanvas.getGraphicsContext2D();
            graphicsContext.setStroke(Color.ORANGERED);
            graphicsContext.setFill(Color.rgb(128, 128, 0, 0.5));
            graphicsContext.setLineWidth(2);

            graphicsContext.beginPath();
            graphicsContext.moveTo(centerX, centerY);
            graphicsContext.lineTo(endsOfLines[0].getX(), endsOfLines[0].getY());
            if ((endsOfLines[0].getX() != endsOfLines[1].getX()) && (endsOfLines[0].getY() != endsOfLines[1].getY())) {
                double anotherX = 0, anotherY = 0;
                if (sides[0] == GameState.SystemSides.Up && sides[1] == GameState.SystemSides.Right) {
                    anotherX = fieldWidth;
                    anotherY = 0;
                }
                else if (sides[0] == GameState.SystemSides.Right && sides[1] == GameState.SystemSides.Down) {
                    anotherX = fieldWidth;
                    anotherY = fieldHeight;
                }
                else if (sides[0] == GameState.SystemSides.Down && sides[1] == GameState.SystemSides.Left) {
                    anotherX = 0;
                    anotherY = fieldHeight;
                }
                else {
                    anotherX = 0;
                    anotherY = 0;
                }
                graphicsContext.lineTo(anotherX, anotherY);
            }
            graphicsContext.lineTo(endsOfLines[1].getX(), endsOfLines[1].getY());
            graphicsContext.lineTo(centerX, centerY);
            graphicsContext.fill();
            graphicsContext.closePath();
        }
    }

    protected Point2D createPointDistantByRadius(double centerX, double centerY, double radius, double angle) {
        double angleInRadius = Math.toRadians(angle);
        return new Point2D(centerX + radius * Math.cos(angleInRadius), centerY + radius * Math.sin(angleInRadius));
    }
}
