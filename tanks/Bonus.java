package tanks;

public class Bonus extends RoundGameObject {

    public Bonus(double startX, double startY, double startDegree, double radius, double velocity, double angularVelocity) {
        super(startX, startY, startDegree, radius, velocity, angularVelocity);
    }

    public Bonus(Bonus obj) {
        this(obj.getCenterX(), obj.getCenterY(), obj.getRotationAngle(), obj.getRadius(), obj.getVelocity(), obj.getAngularVelocity());
    }

    @Override
    public void update(double deltaTime) {

    }

    private BonusType type;

    public BonusType getBonusType() {
        return type;
    }

    public void setBonusType(BonusType type) {
        this.type = type;
    }
}
