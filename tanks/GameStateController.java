package tanks;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

import java.util.HashMap;
import java.util.Map;

public abstract class GameStateController extends Controller {
    @FXML
    protected Canvas gameCanvas;

    @FXML
    protected Canvas backgroundCanvas;

    final private double gameCanvasWidth = 800;
    final private double gameCanvasHeight = 600;
    final private double canvasBorderWidth = 10;

    @FXML
    protected AnchorPane anchorPane;

    protected Image backgroundImage = new Image("/assets/background.png");

    public GameStateController() {
        super();
    }

    @FXML
    void initialize() {
        backgroundCanvas.setWidth(gameCanvasWidth + 2 * canvasBorderWidth);
        backgroundCanvas.setHeight(gameCanvasHeight + 2 * canvasBorderWidth);
        backgroundCanvas.setTranslateX(-canvasBorderWidth);
        backgroundCanvas.setTranslateY(-canvasBorderWidth);
        gameCanvas.setWidth(gameCanvasWidth);
        gameCanvas.setHeight(gameCanvasHeight);
        gameCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(System.nanoTime() - ((GameState) associatedState).getStartTimeInNanos());
            }
        });
    }

    public void clearCanvas() {
        GraphicsContext graphicsContext = gameCanvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
    }

    public void drawBackground() {
        GraphicsContext graphicsContext = gameCanvas.getGraphicsContext2D();
        graphicsContext.save();
        graphicsContext.setGlobalAlpha(0.6);
        graphicsContext.setEffect(new BoxBlur(gameCanvas.getWidth(), gameCanvas.getHeight(), 0));
        graphicsContext.drawImage(backgroundImage, 0, 0);
        graphicsContext.restore();
    }

    public void drawBorder() {
        GraphicsContext graphicsContext = backgroundCanvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.GREEN);
        graphicsContext.setLineWidth(canvasBorderWidth);
        graphicsContext.strokeRect(canvasBorderWidth / 2, canvasBorderWidth / 2, backgroundCanvas.getWidth() - canvasBorderWidth,
                backgroundCanvas.getHeight() - canvasBorderWidth);
    }

    public void drawRotatedImageOnGameCanvas(Image image, double centerX, double centerY, double angle) {
        drawRotatedImage(gameCanvas.getGraphicsContext2D(), image, centerX, centerY, angle);
    }

    protected void drawRotatedImage(GraphicsContext graphicsContext, Image image, double x, double y, double angle) {
        graphicsContext.save();
        rotate(graphicsContext, angle, x, y);
        graphicsContext.drawImage(image, x - image.getWidth() / 2, y - image.getHeight() / 2);
        graphicsContext.restore();
    }

    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }
}
