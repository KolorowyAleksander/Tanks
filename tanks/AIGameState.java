package tanks;

import java.util.ArrayList;
import java.util.List;

public class AIGameState extends GameState {
    protected VisionChecker visionChecker;

    public AIGameState(StateManager stateManager, String fxmlFileName, String playerOneName, String playerTwoName) {
        super(stateManager, fxmlFileName, playerOneName, playerTwoName);

        visionChecker = new VisionChecker(fieldWidth, fieldHeight);

        players[0] = new HumanPlayer(0, playerOneName, gameObjectFactory.createTank(startingPositions[0].getX(), startingPositions[0].getY(),
                90, 32, 50, 50, "tankRudy", playerOneName));
        players[1] = new HumanPlayer(1, playerTwoName, gameObjectFactory.createTank(startingPositions[1].getX(), startingPositions[1].getY(),
                -90, 32, 50, 50, "tankWaffen", playerTwoName));
    }

    protected void updateGame(double deltaTime) {
        sendDataToAIs(players);
        super.updateGame(deltaTime);
    }

    protected void draw() {
        super.draw();
        ((AIGameStateController)controller).drawVisionLines(new Tank[]{players[0].getPlayerTank(), players[1].getPlayerTank()});
    }


    protected void endGame() {
        super.endGame();
        stateManager.pushOnStateStack(new ResultState(stateManager, stateManager.getFXMLFileName("ResultsScene"), "Player one"));
    }

    private void sendDataToAIs(Player[] players) {
        for (ArtificialPlayer player : (ArtificialPlayer[])players) {
            player.receiveVisibleObjectsData(makeListOfObjectVisibleToPlayer(player));
        }
    }

    private List<RoundGameObject> makeListOfObjectVisibleToPlayer(ArtificialPlayer player) {
        Tank playerTank = player.getPlayerTank();
        ArrayList<RoundGameObject> objectsBuffer = new ArrayList<RoundGameObject>();
        for (Player playerWithTank : players) {
            if (playerWithTank != player) {
                Tank tank = playerWithTank.getPlayerTank();
                if (visionChecker.isObjectVisible(playerTank, playerTank.getRangeOfVision(), tank)) {
                    objectsBuffer.add(new Tank(playerWithTank.getPlayerTank()));
                }
            }
        }
        for (Bullet bullet : bullets) {
            if (visionChecker.isObjectVisible(playerTank, playerTank.getRangeOfVision(), bullet)) {
                objectsBuffer.add(new Bullet(bullet));
            }
        }
        return objectsBuffer;
    }
}