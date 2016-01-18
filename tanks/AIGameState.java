package tanks;

import jdk.management.resource.internal.TotalResourceContext;

import java.util.ArrayList;
import java.util.List;

public class AIGameState extends GameState {
    protected VisionChecker visionChecker;
    private static Tournament tournament;
    private double gameDuration = 0;
    private final static double gameDurationLimit = 10;

    public AIGameState(StateManager stateManager, Tournament newTournament, String fxmlFileName, ArtificialPlayer playerOne, ArtificialPlayer playerTwo) {
        super(stateManager, fxmlFileName, playerOne.getPlayerName(), playerTwo.getPlayerName());

        visionChecker = new VisionChecker(fieldWidth, fieldHeight);
        tournament = newTournament;

        players[0] = playerOne;
        players[1] = playerTwo;
    }

    protected void updateGame(double deltaTime) {
        sendDataToAIs(players);
        gameDuration += deltaTime;
        super.updateGame(deltaTime);
    }

    protected void draw() {
        super.draw();
        ((AIGameStateController)controller).drawVisionLines(new Tank[]{players[0].getPlayerTank(), players[1].getPlayerTank()});
    }

    protected boolean checkWhetherTheGameIsOver() {
        if (super.checkWhetherTheGameIsOver() || gameDuration >= gameDurationLimit) {
            return true;
        }
        else {
            return false;
        }
    }

    protected void endGame() {
        super.endGame();
        int result = -1;
        double hpOne = players[0].getPlayerTank().getHealthPoints();
        double hpTwo = players[1].getPlayerTank().getHealthPoints();
        if (hpOne > hpTwo) {
            result = 1;
        }
        else if (hpTwo > hpOne) {
            result = 2;
        }
        else {
            result = 0;
        }
        tournament.endMatch(result);
    }

    private void sendDataToAIs(Player[] players) {
        for (Player player : players) {
            ((ArtificialPlayer)player).receiveVisibleObjectsData(makeListOfObjectVisibleToPlayer((ArtificialPlayer)player));
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