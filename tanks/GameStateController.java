package tanks;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class GameStateController extends Controller {
    private GameState gameState;

    @FXML
    private Canvas gameCanvas;

    final private double canvasBorderWidth = 10;

    @FXML
    private AnchorPane anchorPane;

    private class KeyBindingPair {
        int playerNumber;
        Function function;
    }

    private final EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent> () {
        public void handle(final KeyEvent keyEvent) {
            switch(keyEvent.getCode()) {
                case LEFT:
                    //setRotationInMoveBuffer(0, Move.Rotation.Counterclockwise);
                    break;
                case UP:
                    //setMovementInMoveBuffer(0, Move.Movement.Forward);
                    break;
                case RIGHT:
                    //setRotationInMoveBuffer(0, Move.Rotation.Clockwise);
                    break;
                case DOWN:
                    //setMovementInMoveBuffer(0, Move.Movement.Backward);
                    break;
                case DIGIT2:
                    //setShootingInMoveBuffer(0, Move.Shooting.Shoots);


                case A:
                    //setRotationInMoveBuffer(1, Move.Rotation.Counterclockwise);
                    break;
                case W:
                    //setMovementInMoveBuffer(1, Move.Movement.Forward);
                    break;
                case D:
                    //setRotationInMoveBuffer(1, Move.Rotation.Clockwise);
                    break;
                case S:
                    //setMovementInMoveBuffer(1, Move.Movement.Backward);
                    break;
                case V:
                    //setShootingInMoveBuffer(1, Move.Shooting.Shoots);
            }
        }
    };

    @FXML
    void initialize() {
        gameState = (GameState)associatedState;

        root.setOnKeyPressed(keyEventHandler);
        gameCanvas.setHeight(600);
        gameCanvas.setWidth(800);
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.setLineWidth(10);
        gc.strokeRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        gameCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(System.nanoTime() - ((GameState) associatedState).getStartTimeInNanos());

                Random engine = new Random();
                int width = engine.nextInt(50);
                int height = engine.nextInt(50);
                GraphicsContext gc = gameCanvas.getGraphicsContext2D();
                gc.setFill(Color.GREEN);
                gc.setStroke(Color.BLUE);
                gc.setLineWidth(5);
                gc.strokeOval(event.getX() - width / 2, event.getY() - height / 2, width, height);
            }
        });
    }
}
