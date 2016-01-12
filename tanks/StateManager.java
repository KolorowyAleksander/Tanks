package tanks;

import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class StateManager {
    private Stack<State> stateStack;
    private Stage mainStage;
    private Map<String, String> fxmlFilesNames;

    public StateManager()
    {
        stateStack = new Stack<State>();

        fxmlFilesNames = new HashMap<String, String>();
        fxmlFilesNames.put("MainMenu", "/FXML/MainMenuScene.fxml");
        fxmlFilesNames.put("Game", "/FXML/GameScene.fxml");
    }

    public void startProgram(Stage primaryStage) {
        mainStage = primaryStage;
        mainStage.setResizable(false);

        State mainMenuState = new GameState(fxmlFilesNames.get("Game"));
        stateStack.push(mainMenuState);

        mainStage.setTitle("Tanks");
        mainStage.setScene(stateStack.peek().getScene());
        mainStage.show();
    }
}
