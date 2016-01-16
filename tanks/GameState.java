package tanks;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GameState extends State {
    protected static GameObjectFactory gameObjectFactory;

    protected Player players[];

    protected List<Bullet> bullets;

    protected EventHandler<ActionEvent> gameLoop;

    private Timeline gameLoopInfiniteTimeline;

    protected long startTimeInNanos;
    protected long currentTimeInNanos;

    final static private int framesPerSecond = 30;

    protected double fieldWidth = 800.0, fieldHeight = 600.0;
    protected static final Point2D startingPositions[] = {new Point2D(200, 300), new Point2D(600, 300)};

    public GameState(String fxmlFileName, String playerOneName, String playerTwoName) {
        super(fxmlFileName);

        gameObjectFactory = new GameObjectFactory();

        players = new Player[2];

        bullets = new LinkedList<Bullet>();

        controller.setAssociatedState(this);

        gameLoopInfiniteTimeline = createGameLoopTimeline(framesPerSecond);
        gameLoopInfiniteTimeline.play();

        currentTimeInNanos = startTimeInNanos = System.nanoTime();
    }

    private Timeline createGameLoopTimeline(int framesPerSecond) {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame gameLoopFrame = new KeyFrame(Duration.millis(1000.0 / (double)framesPerSecond), createGameLoop());
        timeline.getKeyFrames().add(gameLoopFrame);

        return timeline;
    }

    private EventHandler<ActionEvent> createGameLoop() {
        gameLoop = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                long newTimeInNanos = System.nanoTime();
                double deltaTime = (double)(newTimeInNanos - currentTimeInNanos) / 1000000000.0;
                currentTimeInNanos = newTimeInNanos;
                updateGame(deltaTime);
                drawOnCanvas();
            }
        };

        return gameLoop;
    }

    public Player getPlayer(int playerNumber) {
        return players[playerNumber];
    }

    protected void drawOnCanvas() {
        GameStateController gameStateController = (GameStateController)controller;
        gameStateController.clearCanvas();
        gameStateController.drawBorder();
        drawTanks();
    }

    private void drawTanks() {
        for (Player player : players) {
            Tank tank = player.getPlayerTank();
            Image image = tank.getImage();
            double x = tank.getCenterX();
            double y = tank.getCenterY();
            double rotationAngle = tank.getRotationAngle();
            ((GameStateController)(controller)).drawRotatedImageOnGameCanvas(image, x, y, rotationAngle);
        }
    }

    protected void updateGame(double deltaTime) {
        updateTanks(deltaTime);
    }

    protected void updateTanks(double deltaTime) {
        for (Player player : players) {
            Move playerMove = player.makeMove();
            Tank playerTank = player.getPlayerTank();
            playerTank.update(deltaTime);
            playerTank.rotate(playerMove.getRotation(), deltaTime);
            playerTank.move(playerMove.getMovement(), deltaTime);
            if (playerMove.getShooting() == Move.Shooting.Shoots && playerTank.isReadyToShoot()) {
                bullets.add(playerTank.shoot());
            }
        }
    }

    public long getStartTimeInNanos() {
        return startTimeInNanos;
    }
}
