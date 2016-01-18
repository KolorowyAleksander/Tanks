package tanks;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class ResultStateController extends Controller {
    @FXML
    Label winnerStatementLabel;
    @FXML
    Button mainMenuButton;
    @FXML
    Button exitButton;

    public void setWinnerStatement(String statement, int fontSize) {
        winnerStatementLabel.setText(statement);
        Font font = new Font("Arial", fontSize);
        winnerStatementLabel.setFont(font);
    }

    @FXML
    public void initialize() {
        mainMenuButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                StateManager stateManager = associatedState.stateManager;
                stateManager.popOutOfStateStack();
                stateManager.refreshScene();
            }
        });
        exitButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                exitGame();
            }
        });
    }
}
