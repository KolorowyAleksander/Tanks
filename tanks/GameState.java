package tanks;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.List;

public class GameState extends State {
    private double fieldWidth = 800.0, fieldHeight = 600.0;

    private Player players[];

    private List<Bullet> bullets;

    private Timeline gameLoopInfiniteTimeline;

    private long startTimeInNanos;
    private long currentTimeInNanos;
    private double counter = 0;

    final static private int framesPerSecond = 30;

    EventHandler<ActionEvent> gameLoop;

    public GameState(String fxmlFileName) {
        super(fxmlFileName);

        players = new Player[2];
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

    private void drawOnCanvas() {
    }

    private void updateGame(double deltaTime) {
    }

    public long getStartTimeInNanos() {
        return startTimeInNanos;
    }
}
