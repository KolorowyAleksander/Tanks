package tanks;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public abstract class GameStateController extends Controller {
    @FXML
    protected Canvas gameCanvas;
    @FXML
    protected Canvas backgroundCanvas;
    @FXML
    protected Canvas playerOneHPCanvas;
    @FXML
    protected Canvas playerTwoHPCanvas;
    @FXML
    protected Label playerOneName;
    @FXML
    protected Label playerTwoName;
    @FXML
    protected Button pauseGameButton;
    @FXML
    protected Button exitGameButton;

    final private double gameCanvasWidth = 800;
    final private double gameCanvasHeight = 600;
    final private double gameCanvasBorderWidth = 10;

    final private double playerDataWidth = 90;
    final private double healthBarHeight = 350;


    @FXML
    protected AnchorPane anchorPane;

    protected Image backgroundImage = new Image("/assets/background.png");

    protected class HealthBar {
        public double topLeftX, topLeftY;
        public double width, height;

        @FXML
        public Canvas canvas;

        public HealthBar(double x, double y, double width, double height, Canvas canvas) {
            this.topLeftX = x;
            this.topLeftY = y;
            this.width = width;
            this.height = height;
            this.canvas = canvas;
            canvas.setTranslateX(x);
            canvas.setTranslateY(y);
            canvas.setWidth(width);
            canvas.setHeight(height);
        }
    }

    protected HealthBar playerOneHealthBar;
    protected HealthBar playerTwoHealthBar;

    public GameStateController() {
        super();
    }

    @FXML
    void initialize() {
        backgroundCanvas.setWidth(gameCanvasWidth + 2 * gameCanvasBorderWidth);
        backgroundCanvas.setHeight(gameCanvasHeight + 2 * gameCanvasBorderWidth);
        backgroundCanvas.setTranslateX(-gameCanvasBorderWidth);
        backgroundCanvas.setTranslateY(-gameCanvasBorderWidth);

        gameCanvas.setWidth(gameCanvasWidth);
        gameCanvas.setHeight(gameCanvasHeight);

        playerOneHealthBar = new HealthBar(0, 200, playerDataWidth, healthBarHeight, playerOneHPCanvas);
        playerTwoHealthBar = new HealthBar(916, 200, playerDataWidth, healthBarHeight, playerTwoHPCanvas);

        exitGameButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                associatedState.stateManager.popOutOfStateStack();
            }
        });
    }

    public void clearCanvas() {
        gameCanvas.getGraphicsContext2D().clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        playerOneHPCanvas.getGraphicsContext2D().clearRect(0, 0, playerOneHPCanvas.getWidth(), playerOneHPCanvas.getHeight());
        playerTwoHPCanvas.getGraphicsContext2D().clearRect(0, 0, playerTwoHPCanvas.getWidth(), playerTwoHPCanvas.getHeight());
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
        graphicsContext.setLineWidth(gameCanvasBorderWidth);
        graphicsContext.strokeRect(gameCanvasBorderWidth / 2, gameCanvasBorderWidth / 2, backgroundCanvas.getWidth() - gameCanvasBorderWidth,
                backgroundCanvas.getHeight() - gameCanvasBorderWidth);
    }

    public void drawPlayersData(Player players[]) {
        for (Player player : players) {
            int playerNumber = player.getPlayerNumber();
            Tank playerTank = player.getPlayerTank();
            double hp = playerTank.getHealthPoints();
            double maxHP = playerTank.getMaxHealthPoints();
            drawPlayerHealthBar(playerNumber, hp, maxHP);
        }
        playerOneName.setText(players[0].getPlayerName());
        playerTwoName.setText(players[1].getPlayerName());
    }

    public void drawPlayerHealthBar(int playerNumber, double healthPoints, double maxHealthPoints) {
        HealthBar healthBar;
        if (playerNumber == 0) {
            healthBar = playerOneHealthBar;
        }
        else{
            healthBar = playerTwoHealthBar;
        }

        drawHealthBar(healthBar, healthPoints, maxHealthPoints);
    }

    protected void drawHealthBar(HealthBar healthBar, double healthPoints, double maxHealthPoints) {
        final double borderWidth = 6;

        Canvas canvas = healthBar.canvas;
        GraphicsContext graphicsContext = healthBar.canvas.getGraphicsContext2D();

        double heightOfHealthBar = (healthPoints / maxHealthPoints) * healthBarHeight;
        double startY = healthBarHeight - heightOfHealthBar;

        graphicsContext.setFill(Color.GREEN);
        graphicsContext.fillRoundRect(0, startY, playerDataWidth, healthBarHeight, borderWidth * 2, borderWidth * 2);

        graphicsContext.setFill(Color.BLACK);
        graphicsContext.setLineWidth(borderWidth);
        graphicsContext.strokeRoundRect(borderWidth / 2, borderWidth / 2, playerDataWidth - borderWidth, healthBarHeight - borderWidth,
                borderWidth, borderWidth);
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
