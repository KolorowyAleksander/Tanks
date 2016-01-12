package tanks;

public class Tank extends RoundGameObject {
    public final static double maxHealthPoints = 1000.0;
    public double healthPoints = 1000.0;
    public double rangeOfVision = 30.0;
    public double angularVelocity;
    private double shootInterval = 0.5;

    public Tank(double startX, double startY, double startDegree, double radius, double velocity, double angularVelocity) {
        super(startX, startY, startDegree, radius, velocity);
        this.angularVelocity = angularVelocity;
    }

    public Bullet shoot() {
        Bullet bullet = new Bullet(center.getX(), center.getY(), rotationAngle, 1, 10, 100);
        return bullet;
    }
}
