package tanks;

import javafx.geometry.Point2D;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

class Match {
    public Match(String one, String two) {
        playersNames = new String[2];
        playersNames[0] = one;
        playersNames[1] = two;
    }

    String playersNames[];
    boolean wasPlayed = false;
    public int winner = -1;
}

public class Tournament {
    public int numberOfPlayers;
    public int currentMatch = 0;
    public int numberOfMatchesToPlay;

    public Match matches[];
    public HashMap<String, Score> scores;

    private StateManager stateManager;
    private GameObjectFactory gameObjectFactory;
    private Point2D startingPositions[];

    String tankImages[] = {"tankRudy", "tankWaffen"};
    double startingRotations[] = {90, 270};

    public Tournament(String playerNames[], StateManager stateManager) {
        numberOfPlayers = playerNames.length;
        numberOfMatchesToPlay = 0;


        matches = new Match[(numberOfPlayers * numberOfPlayers - numberOfPlayers) / 2];
        scores = new HashMap<String, Score>();
        for (int i = 0; i < numberOfPlayers; i++) {
            scores.put(playerNames[i], new Score(playerNames[i], 0));
            for (int j = i + 1; j < numberOfPlayers; j++) {
                matches[numberOfMatchesToPlay++] = new Match(playerNames[i], playerNames[j]);
            }
        }

        this.stateManager = stateManager;
        gameObjectFactory = new GameObjectFactory();
        startingPositions = GameState.getStartingPositions();

        startMatch();
    }

    public void startMatch() {
        ArtificialPlayer players[] = new ArtificialPlayer[2];
        for (int i = 0; i < 2; i++) {
            String playerName = matches[currentMatch].playersNames[i];
            String className = "tanks." + playerName + "AI";

            Tank tank = gameObjectFactory.createGenericTank(startingPositions[i].getX(), startingPositions[i].getY(),
                    startingRotations[i], tankImages[i], playerName);

            try {
                Class aiClass = getAIClass(className);
                Class[] parametersTypes = {Integer.TYPE, Tank.class};
                Constructor constructor = aiClass.getConstructor(parametersTypes);
                Object[] parameters = {i, tank};
                players[i] = (ArtificialPlayer)constructor.newInstance(parameters);
            }
            catch (Exception exception) {
                System.out.println(exception.getMessage());
                System.exit(0);
            }
        }
        String fxmlFileName = stateManager.getFXMLFileName("AIGame");
        stateManager.pushOnStateStack(new AIGameState(stateManager, this, fxmlFileName, players[0], players[1]));
    }

    private Class getAIClass(String className) {
        Class aiClass;
        try {
            aiClass = Class.forName(className);
        }
        catch(Exception exception) {
            System.out.println(exception.getMessage());
            aiClass = ArtificialPlayer.class;
        }
        return aiClass;
    }

    public void endMatch(int result) {
        matches[currentMatch].wasPlayed = true;
        matches[currentMatch].winner = result;

        if (result != 0) {
            String winnerName = matches[currentMatch].playersNames[result - 1];
            scores.get(winnerName).addPoints(3);
        }
        else {
            scores.get(matches[currentMatch].playersNames[0]).addPoints(1);
            scores.get(matches[currentMatch].playersNames[1]).addPoints(1);
        }

        ++currentMatch;
        stateManager.popOutOfStateStack();
        if (currentMatch < numberOfMatchesToPlay) {
            startMatch();
        }
        else {
            showTournamentResults();
        }
    }

    private void showTournamentResults() {
        List<Score> scoresList = new ArrayList(scores.values());
        Collections.sort(scoresList);

        stateManager.pushOnStateStack(new ResultState(stateManager, stateManager.getFXMLFileName("ResultsScene"), scoresList));
    }
}
