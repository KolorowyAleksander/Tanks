package tanks;

public class ResultState extends State {
    String labelStatement = "";

    public ResultState(StateManager stateManager, String fxmlFilename, String winnerName) {
        super(stateManager, fxmlFilename);
        labelStatement = winnerName + " has won";
        ((ResultStateController)controller).setWinnerStatement(labelStatement, 120);
    }

    public ResultState(StateManager stateManager, String fxmlFilename, Score[] scores) {
        super(stateManager, fxmlFilename);
        for (Score score : scores) {
            labelStatement += score.name + " - " + score.points + " points\n";
        }
        ((ResultStateController)controller).setWinnerStatement(labelStatement, 40);
    }
}
