package tanks;

public class ArtificialPlayer extends Player {
    public ArtificialPlayer(int playerNumber, String playerName, Tank tank) {
        super(playerNumber, playerName, tank);
    }

    public Move makeMove() {

        return new Move();
    }
}
