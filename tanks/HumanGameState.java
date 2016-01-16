package tanks;

public class HumanGameState extends GameState {


    public HumanGameState(String fxmlFileName, String playerOneName, String playerTwoName) {
        super(fxmlFileName);

        players = new HumanPlayer[2];
        players[0] = new HumanPlayer(0, playerOneName, gameObjectFactory.createTank(400, 300, 0, 64, 5, 5, "tankRudy", playerOneName));
        players[0] = new HumanPlayer(0, playerTwoName, gameObjectFactory.createTank(400, 300, 0, 64, 5, 5, "tankWaffen", playerTwoName));
    }
}
