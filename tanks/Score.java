package tanks;

public class Score implements Comparable<Score>{
    String playerName;
    int points;

    public Score(String name, int points) {
        playerName = name;
        this.points = points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    @Override
    public int compareTo(Score otherScore) {
        if (points > otherScore.points) {
            return -1;
        }
        else if (points < otherScore.points) {
            return 1;
        }
        else {
            return 0;
        }
    }
}
