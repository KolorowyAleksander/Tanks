package tanks;
class Match {
    public Match(String one, String two) {
        this.playerOneName = one;
        this.playerTwoName = two;
    }

    String playerOneName;
    String playerTwoName;
    boolean wasPlayed = false;
    public int winner = -1;
}

public class Tournament {
    public int numberOfPlayers;
    public int numberOfPlayedMatches = 0;
    public int numberOfMatchesToPlay;

    public Match matches[];

    public Tournament(int numberOfPlayers, String playerNames[]) {
        numberOfMatchesToPlay = (numberOfPlayers * numberOfPlayers - numberOfPlayers) / 2;
        matches = new Match[numberOfMatchesToPlay];
        for (int i = 0; i < numberOfPlayers; i++) {
            for (int j = i + 1; j < numberOfPlayers; j++) {
                matches[i * numberOfPlayers + j - 1] = new Match(playerNames[i], playerNames[j]);
            }
        }
    }

    public Match getMatch() {
        return matches[numberOfPlayedMatches];
    }
}
