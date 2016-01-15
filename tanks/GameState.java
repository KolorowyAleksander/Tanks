package tanks;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import sun.awt.image.ImageWatched;

import java.util.LinkedList;
import java.util.List;

public class GameState extends State {
    private double fieldWidth = 800.0, fieldHeight = 600.0;

    private Player playerOne;
    private Player playerTwo;

    private List<Bullet> bullets;

    private Timeline timeline;

    private long startTimeInNanos;

    public GameState(String fxmlFileName) {
        super(fxmlFileName);

        bullets = new LinkedList<Bullet>();

        KeyFrame keyFrame = new KeyFrame(Duration.millis(16), new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            }
        });
        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();

        startTimeInNanos = System.nanoTime();
    }

    public long getStartTimeInNanos() {
        return startTimeInNanos;
    }
}
