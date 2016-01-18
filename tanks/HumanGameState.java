package tanks;

public class HumanGameState extends GameState {

    public HumanGameState(StateManager stateManager, String fxmlFileName, String playerOneName, String playerTwoName) {
        super(stateManager, fxmlFileName, playerOneName, playerTwoName);

        players[0] = new HumanPlayer(0, playerOneName, gameObjectFactory.createGenericTank(startingPositions[0].getX(), startingPositions[0].getY(),
                90, "tankRudy", playerOneName));
        players[1] = new HumanPlayer(1, playerTwoName, gameObjectFactory.createGenericTank(startingPositions[1].getX(), startingPositions[1].getY(),
                -90, "tankWaffen", playerTwoName));
    }

    protected void updateGame(double deltaTime) {
        ((HumanGameStateController)controller).setMovesBasedOnKeyboard();
        super.updateGame(deltaTime);
    }

    protected void draw() {
        super.draw();
    }

    protected void endGame() {
        double playerOneHealth = players[0].getPlayerTank().getHealthPoints();
        double playerTwoHealth = players[1].getPlayerTank().getHealthPoints();
        Player winner;
        if (playerOneHealth >= playerTwoHealth) {
            winner = players[0];
        }
        else {
            winner = players[1];
        }

        super.endGame();
        stateManager.pushOnStateStack(new ResultState(stateManager, stateManager.getFXMLFileName("ResultsScene"), winner.getPlayerName()));
    }
}
