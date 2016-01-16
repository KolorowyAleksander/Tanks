package tanks;

public class Tank extends RoundGameObject {
    public final static double maxHealthPoints = 1000.0;
    public double healthPoints = 1000.0;
    public double rangeOfVision = 30.0;
    private double shotInterval = 0.5;
    private double timeFromLastShot = 0.0;
    private String ownerName;


    public Tank(double startX, double startY, double startDegree, double radius, double velocity, double angularVelocity) {
        super(startX, startY, startDegree, radius, velocity, angularVelocity);
    }

    public void setOwnerName(String playerName) {
        ownerName = playerName;
    }

    public Bullet shoot() {
        timeFromLastShot = 0;
        System.out.println("Shooting");
        return new Bullet(getCenterX(), getCenterY(), rotationAngle, 1, 10, 100);
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
