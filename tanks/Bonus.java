package tanks;

public class Bonus extends RoundGameObject {

    public Bonus(double startX, double startY, double startDegree, double radius, double velocity, double angularVelocity) {
        super(startX, startY, startDegree, radius, velocity, angularVelocity);
    }

    public Bonus(Bonus otherBonus) {
        super(otherBonus.getCenterX(), otherBonus.getCenterY(), otherBonus.getRotationAngle(), otherBonus.getRadius(),
                otherBonus.getVelocity(), otherBonus.getAngularVelocity());
        this.type = otherBonus.getBonusType();
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
