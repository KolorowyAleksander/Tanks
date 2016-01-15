package tanks;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.List;

abstract public class GameState extends State {
    protected Player players[];

    protected List<Bullet> bullets;

    private Timeline gameLoopInfiniteTimeline;

    protected long startTimeInNanos;
    protected long currentTimeInNanos;

    final static private int framesPerSecond = 30;
    protected double fieldWidth = 800.0, fieldHeight = 600.0;

    protected EventHandler<ActionEvent> gameLoop;

    public GameState(String fxmlFileName) {
        super(fxmlFileName);

        bullets = new LinkedList<Bullet>();

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
        EventHandler<ActionEvent> gameLoop = new EventHandler<ActionEvent>() {
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

    abstract protected void drawOnCanvas();

    abstract protected void updateGame(double deltaTime);

    public long getStartTimeInNanos() {
        return startTimeInNanos;
    }
}
