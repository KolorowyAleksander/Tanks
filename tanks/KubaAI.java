package tanks;

public class KubaAI extends ArtificialPlayer {
    public KubaAI (int playerNumber, String playerName, Tank tank) {
        super(playerNumber, playerName, tank);
    }

    public static Move.Movement getRandomMovement() {
        return Move.Movement.values()[(int)(Math.random() * Move.Movement.values().length)];
    }

    public static Move.Rotation getRandomRotation() {
        return Move.Rotation.values()[(int)(Math.random() * Move.Rotation.values().length)];
    }

    public static Move.Shooting getRandomShooting() {
        return Move.Shooting.values()[(int)(Math.random() * Move.Shooting.values().length)];
    }

    public Move makeMove(double deltaTime) {
        return new Move(getRandomMovement(), getRandomRotation(), getRandomShooting());
    }
}
