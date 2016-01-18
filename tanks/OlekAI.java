package tanks;

public class OlekAI extends ArtificialPlayer {
    private final static double changeMovementTime = 2.0, changeRotationTime = 0.5;
    private double lastChangingOfMovement = 0.0, lastChangingOfRotation = 0.0;

    private Move.Movement currentMovement;
    private Move.Rotation currentRotation;

    public OlekAI (int playerNumber, String playerName, Tank tank) {
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
        lastChangingOfMovement += deltaTime;
        lastChangingOfRotation += deltaTime;
        if (lastChangingOfMovement >= changeMovementTime) {
            currentMovement = getRandomMovement();
            lastChangingOfMovement = 0;
        }
        if (lastChangingOfRotation >= changeRotationTime) {
            currentRotation = getRandomRotation();
            lastChangingOfRotation = 0;
        }

        return new Move(currentMovement, currentRotation, Move.Shooting.Shoots);
    }
}
