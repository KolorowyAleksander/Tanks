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
        fxmlFilesNames.put("AIGame", "/FXML/AIGameScene.fxml");
        fxmlFilesNames.put("ResultsScene", "/FXML/ResultScene.fxml");
    }

    public String getFXMLFileName(String stateName) {
        return fxmlFilesNames.get(stateName);
    }

    public void startProgram(Stage primaryStage) {
        mainStage = primaryStage;
        mainStage.setResizable(false);

        State mainMenuState = new MainMenuState(this, getFXMLFileName("MainMenu"));
        //State mainMenuState = new HumanGameState(fxmlFilesNames.get("HumanGame"), "Player One", "Player Two");
        stateStack.push(mainMenuState);

        mainStage.setTitle("Tanks");
        refreshScene();
        mainStage.show();
    }

    public void refreshScene() {
        mainStage.setWidth(1040);
        mainStage.setHeight(800);
        mainStage.setScene(stateStack.peek().getScene());
    }

    public void pushOnStateStack(State newState) {
        stateStack.push(newState);
        refreshScene();
    }

    public void popOutOfStateStack() {
        stateStack.pop();
    }

    public void closeProgram() {
        mainStage.close();
    }
}
