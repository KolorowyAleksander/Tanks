package tanks;

public class Move {
    enum Movement {Forward, Backward};
    Movement movement;

    enum Rotation {Clockwise, Counterclockwise};
    Rotation rotation;

    boolean isShooting;

    public Move(Movement movement, Rotation rotation, boolean isShooting) {
        this.movement = movement;
        this.rotation = rotation;
        this.isShooting = isShooting;
    }
}
