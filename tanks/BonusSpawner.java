package tanks;

import javafx.scene.shape.Circle;

import java.util.Random;

public class BonusSpawner {
    private GameObjectFactory gameObjectFactory;
    private CollisionChecker collisionChecker;

    private double timeFromLastSpawn = 0;
    private double timeToNextSpawn = 0;

    private static double bonusRadius = 24;

    Random randomEngine;

    public BonusSpawner(CollisionChecker collisionChecker) {
        this.collisionChecker = collisionChecker;
        gameObjectFactory = new GameObjectFactory();
        randomEngine = new Random();
    }

    private double getRandomDouble(double min, double max) {
        return min + (max - min) * randomEngine.nextDouble();
    }

    Bonus getNewBonus (Tank tankOne, Tank tankTwo) {
        if (timeFromLastSpawn >= timeToNextSpawn) {
            double x = 0, y = 0;
            Bonus tempBonus;

            do {
                x = getRandomDouble(0, collisionChecker.boundariesWidth);
                y = getRandomDouble(0, collisionChecker.boundariesHeight);
                tempBonus = gameObjectFactory.createBonus(x, y, bonusRadius);
            }
            while (collisionChecker.isColliding(tempBonus, tankOne) || collisionChecker.isColliding(tempBonus, tankTwo) ||
                    collisionChecker.isObjectOutsideOfBoundaries(tempBonus));

            timeFromLastSpawn = 0;
            timeToNextSpawn = getRandomDouble(5, 15);

            return tempBonus;
        }
        else {
            return null;
        }
    }

    public boolean isSpawnReady() {
        if (timeFromLastSpawn >= timeToNextSpawn) {
            return true;
        }
        else {
            return false;
        }
    }

    public void update(double deltaTime){
        timeFromLastSpawn += deltaTime;
    }


}
