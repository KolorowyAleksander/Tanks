package tanks;

public class Tank extends RoundGameObject {
    private final static double maxHealthPoints = 1000.0;
    private double healthPoints = 1000.0;
    private double rangeOfVision = 30.0;
    private double shotInterval = 1.0;
    private double timeFromLastShot = 0.0;
    private String ownerName;


    public Tank(double startX, double startY, double startDegree, double radius, double velocity, double angularVelocity) {
        super(startX, startY, startDegree, radius, velocity, angularVelocity);
        timeFromLastShot = shotInterval;
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

    public double getHealthPoints() {
        return healthPoints;
    }

    public static double getMaxHealthPoints() {return maxHealthPoints;}

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
