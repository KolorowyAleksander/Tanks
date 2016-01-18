package tanks;

import java.util.ArrayList;
import java.util.List;

public class AIGameState extends GameState {
    protected VisionChecker visionChecker;
    private static Tournament tournament;

    public AIGameState(StateManager stateManager, String fxmlFileName, Tournament tournament) {
        super(stateManager, fxmlFileName, tournament.matches[0].playerOneName, tournament.matches[0].playerTwoName);
        this.tournament = tournament;
        Match newMatch = tournament.getMatch();

        visionChecker = new VisionChecker(fieldWidth, fieldHeight);

        players[0] = new KubaAI(0, "Kuba", gameObjectFactory.createGenericTank(startingPositions[0].getX(), startingPositions[0].getY(),
                90, "tankWaffen", newMatch.playerOneName));
        players[1] = new AgataAI(1, "Olek", gameObjectFactory.createGenericTank(startingPositions[1].getX(), startingPositions[1].getY(),
                -90, "tankRudy", newMatch.playerTwoName));
    }

    public AIGameState(StateManager stateManager, String fxmlFileName, String playerOneName, String playerTwoName) {
        super(stateManager, fxmlFileName, playerOneName, playerTwoName);

        visionChecker = new VisionChecker(fieldWidth, fieldHeight);

        players[0] = new OlekAI(0, "Olek", gameObjectFactory.createGenericTank(startingPositions[0].getX(), startingPositions[0].getY(),
                90, "tankWaffen", playerOneName));
        players[1] = new AgataAI(1, "Agata", gameObjectFactory.createGenericTank(startingPositions[1].getX(), startingPositions[1].getY(),
                270, "tankRudy", playerOneName));
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