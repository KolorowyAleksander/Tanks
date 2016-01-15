package tanks;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import java.util.Random;

public class GameStateController extends Controller {

    private GameState gameState;

    @FXML
    private Canvas gameCanvas;

    final private double canvasBorderWidth = 10;

    @FXML
    private AnchorPane anchorPane;

    private final EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent> () {
        public void handle(final KeyEvent keyEvent) {
            switch(keyEvent.getCode()) {
                case LEFT:
                    System.out.println("Left");
                    break;
                case UP:
                    System.out.println("Up");
                    break;
                case RIGHT:
                    System.out.println("Right");
                    break;
                case DOWN:
                    System.out.println("Down");
                    break;
            }
        }
    };

    @FXML
    void initialize() {
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
