package tanks;

public class AgataAI extends ArtificialPlayer {
    boolean startPosition = false;
    Move.Rotation startRotation;
    int counter = 0;

    private boolean seeTank() {
        for (RoundGameObject element : visibleObjectsBuffer) {
            if (element instanceof Tank)
                return true;
        }
        return false;
    }

    public AgataAI(int playerNumber, String playerName, Tank tank) {
        super(playerNumber, playerName, tank);
        if (getPlayerTank().getCenterX() < 400) {
            startRotation = Move.Rotation.CounterClockwise;
        } else {
            startRotation = Move.Rotation.Clockwise;
        }
    }

    public static Move.Movement getRandomMovement() {
        return Move.Movement.values()[(int) (Math.random() * Move.Movement.values().length)];
    }

    public static Move.Rotation getRandomRotation() {
        return Move.Rotation.values()[(int) (Math.random() * Move.Rotation.values().length)];
    }

    public static Move.Shooting getRandomShooting() {
        return Move.Shooting.values()[(int) (Math.random() * Move.Shooting.values().length)];
    }

    public Move makeMove(double deltaTime) {
        if (seeTank()) {
            return new Move(Move.Movement.Staying, Move.Rotation.Staying, Move.Shooting.Shoots);
        } else {
            if ((startPosition == false) && (getPlayerTank().getRotationAngle() < 10 || getPlayerTank().getRotationAngle() > 350)) {
                startPosition = true;
                if (startRotation == Move.Rotation.Clockwise) {
                    startRotation = Move.Rotation.CounterClockwise;
                } else {
                    startRotation = Move.Rotation.Clockwise;
                }
            }

            if (startPosition == false) {
                return new Move(Move.Movement.Staying, startRotation, Move.Shooting.Shoots);
            } else {
                if (counter < 3) {
                    counter++;
                    return new Move(Move.Movement.Forward, Move.Rotation.Staying, Move.Shooting.Shoots);
                } else {
                    counter = 0;
                    return new Move(Move.Movement.Forward, startRotation, Move.Shooting.Shoots);
                }
            }
        }
    }
}
