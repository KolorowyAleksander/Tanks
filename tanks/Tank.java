package tanks;

public class Tank extends RoundGameObject {
    private final static double maxHealthPoints = 1000.0;
    private double healthPoints = 1000.0;
    private double rangeOfVision = 30.0;
    private double shotInterval = 1.0;
    private double timeFromLastShot = 0.0;
    private final static double startArmorPoints = 100.0;
    private double armor = startArmorPoints;
    private String ownerName;

    {
        timeFromLastShot = shotInterval;
    }

    public Tank(double startX, double startY, double startDegree, double radius, double velocity, double angularVelocity) {
        super(startX, startY, startDegree, radius, velocity, angularVelocity);
    }

    public Tank(Tank otherTank) {
        super(otherTank);
    }

    public void setOwnerName(String playerName) {
        ownerName = new String(playerName);
    }

    public Bullet shoot(GameObjectFactory factory) {
        timeFromLastShot = 0;
        double angleInRadians = Math.toRadians(rotationAngle);
        double gunLength = getRadius() + 2;
        double deltaBulletX = gunLength * Math.sin(angleInRadians);
        double deltaBulletY = gunLength * Math.cos(angleInRadians);
        return factory.createBullet(getCenterX() + deltaBulletX, getCenterY() - deltaBulletY, rotationAngle, 4, 200, 100);
    }

    public void addHealthPoints(double points) {
        healthPoints += points;
        if (healthPoints > maxHealthPoints) {
            healthPoints = maxHealthPoints;
        }
    }

    public void addSpeed(double points) {
        velocity += points;
    }

    public void addRotationSpeed(double points) {angularVelocity += points;}

    public void addVision(double points) {
        rangeOfVision += points;
    }

    public void addArmor(double points) {armor += points;}

    public double getHealthPoints() {
        return healthPoints;
    }

    public static double getMaxHealthPoints() {return maxHealthPoints;}

    public double getArmorPoints() {return armor;}

    public static double getStartArmorPoints() {return startArmorPoints;}

    @Override
    public void update(double deltaTime) {
        timeFromLastShot += deltaTime;
    }

    public double getShotInterval() {
        return shotInterval;
    }

    public boolean isReadyToShoot() {
        if (timeFromLastShot >= shotInterval) {
            return true;
        }
        else {
            return false;
        }
    }

    public double getRangeOfVision() {
        return rangeOfVision;
    }
}
