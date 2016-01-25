package tanks;

public class IdleAI extends ArtificialPlayer {
    public IdleAI(int playerNumber, Tank tank) {
        super(playerNumber, "Idle", tank);
    }

    public Move makeMove(double deltaTime) {
        return new Move(Move.Movement.Staying, Move.Rotation.Staying, Move.Shooting.NotShoots);
    }
}
