package tanks;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class GameStateController extends Controller {
    protected GameState gameState;

    @FXML
    protected Canvas gameCanvas;

    final private double canvasBorderWidth = 10;

    @FXML
    protected AnchorPane anchorPane;


    public GameStateController() {
        super();
        gameState = (GameState)associatedState;
    }

    @FXML
    void initialize() {
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
            }
        });
    }

    protected void drawRotatedImage(GraphicsContext graphicsContext, Image image, double x, double y, double angle) {
        graphicsContext.save();
        graphicsContext.rotate(angle);
        graphicsContext.drawImage(image, x, y);
        graphicsContext.restore();
    }
}
