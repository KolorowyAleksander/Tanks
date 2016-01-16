package tanks;

import javafx.scene.image.Image;

public class Bullet extends RoundGameObject {
    private double damage;

    public Bullet(double startX, double startY, double startDegree, double radius, double velocity, double damage) {
        super(startX, startY, startDegree, radius, velocity, 0);
        this.damage = damage;
    }

    public void update(double deltaTime) {}
}
