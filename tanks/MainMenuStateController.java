package tanks;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class MainMenuStateController extends Controller{
    @FXML
    protected Button startHumanGameButton;
    @FXML
    protected Button startAITournamentButton;
    @FXML
    protected Button exitGameButton;

    public MainMenuStateController() {
        super();
    }

    private void startHumanGame() {
        StateManager stateManager = associatedState.stateManager;
        String fxmlFileName = stateManager.getFXMLFileName("HumanGame");
        stateManager.pushOnStateStack(new HumanGameState(stateManager, fxmlFileName, "Player One", "Player Two"));
    }

    private void startAITournament() {
        StateManager stateManager = associatedState.stateManager;
        String fxmlFileName = stateManager.getFXMLFileName("AIGame");
        stateManager.pushOnStateStack(new AIGameState(stateManager, fxmlFileName, "Player One", "Player Two"));
    }

    void initialize() {
        startHumanGameButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startHumanGame();
            }
        });
        startAITournamentButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startAITournament();
            }
        });
        exitGameButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                exitGame();
            }
        });
    }
}
