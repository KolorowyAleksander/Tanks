package tanks;

public class Tank extends RoundGameObject {
    public final static double maxHealthPoints = 1000.0;
    public double healthPoints = 1000.0;
    public double rangeOfVision = 30.0;
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
        return factory.createBullet(getCenterX(), getCenterY(), rotationAngle, 8, 200, 100);
    }

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
}
