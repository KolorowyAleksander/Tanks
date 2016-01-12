package tanks;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;

public class GameStateController extends Controller {

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
    }
}
