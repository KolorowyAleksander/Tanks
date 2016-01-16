package tanks;

import java.util.EnumMap;

public class Move {
    enum MoveSections {Movement, Rotation, Shooting};

    enum Movement {Staying, Forward, Backward};
    enum Rotation {Staying, Clockwise, CounterClockwise};
    enum Shooting {NotShoots, Shoots}

    private EnumMap<MoveSections, Enum> sections = new EnumMap<MoveSections, Enum>(MoveSections.class);

    public Move(Movement movement, Rotation rotation, Shooting shooting) {
        sections.put(MoveSections.Movement, movement);
        sections.put(MoveSections.Rotation, rotation);
        sections.put(MoveSections.Shooting, shooting);    }

    public Move() {
        sections.put(MoveSections.Movement, Movement.Staying);
        sections.put(MoveSections.Rotation, Rotation.Staying);
        sections.put(MoveSections.Shooting, Shooting.NotShoots);
    }

    public void setMoveSection(MoveSections section, Enum value) {
        sections.put(section, value);
    }

    public Movement getMovement() {return (Movement) sections.get(MoveSections.Movement);}
    public Rotation getRotation() {return (Rotation) sections.get(MoveSections.Rotation);}
    public Shooting getShooting() {return (Shooting) sections.get(MoveSections.Shooting);}
}
