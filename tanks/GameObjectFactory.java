package tanks;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameObjectFactory {
    private static ImageLoader imageLoader;

    public GameObjectFactory() {
        loadImageAlbum();
    }

    private void loadImageAlbum() {
        imageLoader = new ImageLoader();
        Map<String, String> imageEntries = new HashMap<String, String>();

        imageEntries.put("/assets/tanks/tankGray.png", "tankGray");
        imageEntries.put("/assets/tanks/tankRudy.png", "tankRudy");
        imageEntries.put("/assets/tanks/tankWaffen.png", "tankWaffen");
        imageEntries.put("/assets/bullet.png", "bullet");
        imageEntries.put("/assets/boosts/boosts_health.png", "health");
        imageEntries.put("/assets/boosts/boosts_shield_red.png", "shield");
        imageEntries.put("/assets/boosts/boosts_sight.png", "sight");
        imageEntries.put("/assets/boosts/boosts_speed_red.png", "speed");

        imageLoader.putMapIntoImageAlbum(imageEntries);
    }

    public Tank createTank(double startX, double startY, double startDegree, double radius, double velocity, double angularVelocity, String imageName, String playerName) {
        Tank newTank = new Tank(startX, startY, startDegree, radius, velocity, angularVelocity);
        newTank.setImage(imageLoader.getImageFromAlbum(imageName));
        newTank.setOwnerName(playerName);

        return newTank;
    }

    public Bullet createBullet(double startX, double startY, double startDegree, double radius, double velocity, double damage) {
        Bullet newBullet = new Bullet(startX, startY, startDegree, radius, velocity, damage);
        newBullet.setImage(imageLoader.getImageFromAlbum("bullet"));

        return newBullet;
    }

    public Bonus createBonus(double x, double y, double radius) {
        Bonus newBonus = new Bonus(x, y, 0, radius, 0, 0);
        BonusType type = BonusType.getRandom();
        newBonus.setBonusType(type);
        newBonus.setImage(imageLoader.getImageFromAlbum(BonusType.getString(type)));

        return newBonus;
    }

    private void setImage(RoundGameObject gameObject, String name) {
        gameObject.setImage(imageLoader.getImageFromAlbum(name));
    }
}
