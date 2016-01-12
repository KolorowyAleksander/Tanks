package tanks;

import com.sun.org.apache.xml.internal.security.Init;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import tanks.State;

import java.net.URL;
import java.util.ResourceBundle;

public class GameState extends State {
    private double fieldWidth = 800.0, fieldHeight = 600.0;

    private IPlayer playerOne;
    private IPlayer playerTwo;

    Timeline timeline;

    public GameState(String fxmlFileName) {
        super(fxmlFileName);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(16), new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                System.out.println("Tick");
            }
        });
        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
}
