package tanks;

import java.util.List;

public class ResultState extends State {
    String labelStatement = "";

    public ResultState(StateManager stateManager, String fxmlFilename, String winnerName) {
        super(stateManager, fxmlFilename);
        labelStatement = winnerName + " has won";
        ((ResultStateController)controller).setWinnerStatement(labelStatement, 120);
    }

    public ResultState(StateManager stateManager, String fxmlFilename, List<Score> scores) {
        super(stateManager, fxmlFilename);
        for (Score score : scores) {
            labelStatement += score.playerName + " - " + score.points + " points\n";
        }
        ((ResultStateController)controller).setWinnerStatement(labelStatement, 40);
    }
}
