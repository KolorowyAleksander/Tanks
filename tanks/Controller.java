package tanks;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;

abstract public class Controller {
    @FXML
    protected Parent root;

    public Parent getRoot() {
        return root;
    }

    @FXML
    abstract void initialize();
}