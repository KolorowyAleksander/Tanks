package tanks;

public class Move {
    enum Movement {Staying, Forward, Backward};
    public Movement movement;

    enum Rotation {Staying, Clockwise, Counterclockwise};
    public Rotation rotation;

    enum Shooting {NotShoots, Shoots}
    public Shooting shooting;

    public Move(Movement movement, Rotation rotation, Shooting shooting) {
        this.movement = movement;
        this.rotation = rotation;
        this.shooting = shooting;
    }

    public Move() {
        this.movement = Movement.Staying;
        this.rotation = Rotation.Staying;
        this.shooting = Shooting.NotShoots;
    }
}
