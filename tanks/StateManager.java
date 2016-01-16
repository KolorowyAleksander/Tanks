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
        fxmlFilesNames.put("HumanGame", "/FXML/HumanGameScene.fxml");
    }

    public void startProgram(Stage primaryStage) {
        mainStage = primaryStage;
        mainStage.setResizable(false);

        State mainMenuState = new HumanGameState(fxmlFilesNames.get("HumanGame"), "Kuba", "Olek");
        stateStack.push(mainMenuState);

        mainStage.setTitle("Tanks");
        mainStage.setScene(stateStack.peek().getScene());
        mainStage.show();
    }
}
