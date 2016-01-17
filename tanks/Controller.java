package tanks;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;

public abstract class Controller {
    protected State associatedState;

    @FXML
    protected Parent root;

    public Parent getRoot() {
        return root;
    }

    public void setAssociatedState(State associatedState) {
        this.associatedState = associatedState;
    }

    @FXML
    abstract void initialize();

    protected void exitGame() {
        associatedState.stateManager.closeProgram();
    }
}