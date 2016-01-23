package tanks;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.concurrent.ConcurrentLinkedQueue;

public class GameState extends State {
    protected static GameObjectFactory gameObjectFactory;
    protected static CollisionChecker collisionChecker;
    protected static BonusSpawner bonusSpawner;

    protected Player players[];

    protected ConcurrentLinkedQueue<Bullet> bullets;
    protected ConcurrentLinkedQueue<Bonus> bonuses;

    protected EventHandler<ActionEvent> gameLoop;

    protected Timeline gameLoopInfiniteTimeline;

    protected long startTimeInNanos;
    protected long currentTimeInNanos;

    final static private int framesPerSecond = 30;

    protected static double fieldWidth = 800.0, fieldHeight = 600.0;
    protected double damageOnTanksCollision = 100.0;
    protected static final Point2D startingPositions[] = {new Point2D(200, 300), new Point2D(600, 300)};

    public static double getFieldWidth() {
        return fieldWidth;
    }

    public static double getFieldHeight() {
        return fieldHeight;
    }

    public static Point2D[] getStartingPositions() {
        return startingPositions;
    }

    public GameState(StateManager stateManager, String fxmlFileName, String playerOneName, String playerTwoName) {
        super(stateManager, fxmlFileName);

        gameObjectFactory = new GameObjectFactory();
        collisionChecker = new CollisionChecker(fieldWidth, fieldHeight);
        bonusSpawner = new BonusSpawner(collisionChecker);

        players = new Player[2];

        bullets = new ConcurrentLinkedQueue<Bullet>();
        bonuses = new ConcurrentLinkedQueue<Bonus>();

        controller.setAssociatedState(this);

        gameLoopInfiniteTimeline = createGameLoopTimeline(framesPerSecond);
        gameLoopInfiniteTimeline.play();

        currentTimeInNanos = startTimeInNanos = System.nanoTime();
    }

    private Timeline createGameLoopTimeline(int framesPerSecond) {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame gameLoopFrame = new KeyFrame(Duration.millis(1000.0 / (double) framesPerSecond), createGameLoop());
        timeline.getKeyFrames().add(gameLoopFrame);

        return timeline;
    }

    private EventHandler<ActionEvent> createGameLoop() {
        gameLoop = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                long newTimeInNanos = System.nanoTime();
                double deltaTime = (double) (newTimeInNanos - currentTimeInNanos) / 1000000000.0;
                currentTimeInNanos = newTimeInNanos;
                updateGame(deltaTime);
                draw();
                if (checkWhetherTheGameIsOver()) {
                    endGame();
                }
            }
        };

        return gameLoop;
    }

    public Player getPlayer(int playerNumber) {
        return players[playerNumber];
    }

    protected void draw() {
        GameStateController gameStateController = (GameStateController) controller;
        gameStateController.clearCanvas();
        gameStateController.drawBackground();
        gameStateController.drawBorder();
        drawTanks();
        drawBullets();
        drawBonuses();
        drawPlayersData();
    }

    protected void drawPlayersData() {
        ((GameStateController) controller).drawPlayersData(players);
    }

    protected void drawTanks() {
        for (Player player : players) {
            Tank tank = player.getPlayerTank();
            drawGameObjectOnCanvas(tank);
        }
    }

    protected void drawBullets() {
        for (Bullet bullet : bullets) {
            drawGameObjectOnCanvas(bullet);
        }
    }

    protected void drawBonuses() {
        for (Bonus bonus : bonuses) {
            drawGameObjectOnCanvas(bonus);
        }
    }

    protected void drawGameObjectOnCanvas(RoundGameObject gameObject) {
        ((GameStateController) (controller)).drawRotatedImageOnGameCanvas(gameObject.getImage(), gameObject.getCenterX(),
                gameObject.getCenterY(), gameObject.getRotationAngle());
    }

    protected void updateGame(double deltaTime) {
        updateTanks(deltaTime);
        updateBullets(deltaTime);
        updateBonuses(deltaTime);
        checkCollisions();
        deleteBulletsOutsideOfBorders();
        checkWhetherTheGameIsOver();
    }

    protected void updateTanks(double deltaTime) {
        for (Player player : players) {
            Move playerMove = player.makeMove(deltaTime);
            Tank playerTank = player.getPlayerTank();

            playerTank.update(deltaTime);

            playerTank.rotate(playerMove.getRotation(), deltaTime);

            if (collisionChecker.checkWhetherMoveIsPossible(playerTank, deltaTime, playerMove.getMovement())) {
                playerTank.move(playerMove.getMovement(), deltaTime);
            }

            if ((playerMove.getShooting() == Move.Shooting.Shoots) && (playerTank.isReadyToShoot())) {
                bullets.add(playerTank.shoot(gameObjectFactory));
            }
        }
    }

    protected void updateBullets(double deltaTime) {
        for (Bullet bullet : bullets) {
            bullet.update(deltaTime);
            bullet.move(Move.Movement.Forward, deltaTime);
        }
    }

    protected void updateBonuses(double deltaTime) {
        bonusSpawner.update(deltaTime);
        if(bonusSpawner.isSpawnReady()) {
            bonuses.add(bonusSpawner.getNewBonus(players[0].getPlayerTank(), players[1].getPlayerTank()));
        }
        for (Bonus bonus : bonuses) {
            bonus.update(deltaTime);
        }
    }


    protected void checkCollisions() {
        ConcurrentLinkedQueue<RoundGameObject> gameObjects = new ConcurrentLinkedQueue<RoundGameObject>();
        gameObjects.addAll(bullets);
        gameObjects.addAll(bonuses);
        for (Player player : players) {
            gameObjects.add(player.getPlayerTank());
        }

        for (RoundGameObject gameObjectA : gameObjects) {
            for (RoundGameObject gameObjectB : gameObjects) {
                if (gameObjectA != gameObjectB) {
                    if (collisionChecker.isColliding(gameObjectA, gameObjectB)) {
                        onCollision(gameObjectA, gameObjectB);
                    }
                }
            }
        }
    }

    protected void onCollision(RoundGameObject gameObjectA, RoundGameObject gameObjectB) {
        if ((gameObjectA instanceof Bullet || gameObjectA instanceof Bonus) && gameObjectB instanceof Tank) {
            swap(gameObjectA, gameObjectB);
        } else if (gameObjectA instanceof Bonus && gameObjectB instanceof Bullet) {
            return;
        }

        if (gameObjectA instanceof Tank && gameObjectB instanceof Tank) {
            final double shiftMultiplier = 2;

            ((Tank) gameObjectA).addHealthPoints(-damageOnTanksCollision);
            ((Tank) gameObjectB).addHealthPoints(-damageOnTanksCollision);

            Point2D centerA = gameObjectA.getCenterPoint();
            Point2D centerB = gameObjectB.getCenterPoint();

            Point2D center = centerA.midpoint(centerB);

            Point2D shiftVector = centerA.add(center.multiply(-1));

            Point2D newCenterA = centerA.add(shiftVector.multiply(shiftMultiplier));
            Point2D newCenterB = centerB.add(shiftVector.multiply(-shiftMultiplier));

            if (collisionChecker.isCircleInsideOfBoundaries(new Circle(newCenterA.getX(), newCenterA.getY(), gameObjectA.getRadius()))) {
                gameObjectA.moveTo(newCenterA);
            } else {
                gameObjectA.moveTo(shiftPointOnTankCollision(centerA, center, gameObjectA.getRadius()));
            }

            if (collisionChecker.isCircleInsideOfBoundaries(new Circle(newCenterB.getX(), newCenterB.getY(), gameObjectB.getRadius()))) {
                gameObjectB.moveTo(newCenterB);
            } else {
                gameObjectB.moveTo(shiftPointOnTankCollision(centerB, center, gameObjectB.getRadius()));
            }
        } else if (gameObjectA instanceof Tank && gameObjectB instanceof Bullet) {
            ((Tank) gameObjectA).addHealthPoints(-((Bullet) gameObjectB).getDamage());
            bullets.remove(gameObjectB);
        } else if (gameObjectA instanceof Tank && gameObjectB instanceof Bonus) {
            switch (((Bonus) gameObjectB).getBonusType()) {
                case ARMOR:
                    ((Tank) gameObjectA).addHealthPoints(300);
                    break;
                case HEALTH:
                    ((Tank) gameObjectA).addHealthPoints(100);
                    break;
                case SPEED:
                    ((Tank) gameObjectA).addSpeed(3);
                    break;
                case VISION:
                    ((Tank) gameObjectA).addVision(3);
                    break;
                default:
                    break;
            }
            bonuses.remove(gameObjectB);
        } else if (gameObjectA instanceof Bullet && gameObjectB instanceof Bullet) {
            return;
        } else if (gameObjectA instanceof Bullet && gameObjectB instanceof Bonus) {
            return;
        } else if (gameObjectA instanceof Bonus && gameObjectB instanceof Bonus) {
            return;
        }
    }

    protected Point2D shiftPointOnTankCollision(Point2D currentPosition, Point2D center, double radius) {
        double posX = currentPosition.getX(), posY = currentPosition.getY();
        double centerX = center.getX(), centerY = center.getY();

        double a = (centerY - posY) / (centerX - posX);
        double angle = Math.toDegrees(Math.atan2(posY - centerY, posX - centerX));

        SystemSides side = findSystemSide(currentPosition, angle, fieldWidth, fieldHeight);

        Point2D crossPoint = getCrossPointWithSide(currentPosition, angle, fieldWidth, fieldHeight, side);

        double newCenterX = 0, newCenterY = 0;

        switch (side) {
            case Right:
                newCenterX = fieldWidth - radius;
                newCenterY = crossPoint.getY() - (radius * a);
                break;

            case Down:
                newCenterX = crossPoint.getX() - (radius / a);
                newCenterY = fieldHeight - radius;
                break;

            case Left:
                newCenterX = radius;
                newCenterY = crossPoint.getY() + (radius * a);
                break;

            case Up:
                newCenterX = crossPoint.getX() + (radius / a);
                newCenterY = radius;
                break;
        }

        return new Point2D(newCenterX, newCenterY);
    }

    protected Point2D getCrossPointWithSide(Point2D point, double rotationAngleInDegrees, double fieldWidth, double fieldHeight, SystemSides side) {
        double centerX = point.getX(), centerY = point.getY();

        double angle = (rotationAngleInDegrees + 360) % 360;

        if (angle == 90) {
            return new Point2D(centerX, fieldHeight);
        } else if (angle == 270) {
            return new Point2D(centerX, 0);
        }

        double a = Math.tan(Math.toRadians(angle));
        double b = centerY - a * centerX;

        switch (side) {
            case Right:
                return new Point2D(fieldWidth, a * fieldWidth + b);
            case Down:
                return new Point2D((fieldHeight - b) / a, fieldHeight);
            case Left:
                return new Point2D(0, b);
            case Up:
                return new Point2D(-b / a, 0);
            default:
                return new Point2D(0, 0);
        }
    }

    protected SystemSides findSystemSide(Point2D point, double rotationAngleInDegrees, double fieldWidth, double fieldHeight) {
        double centerX = point.getX(), centerY = point.getY();
        double angle = (rotationAngleInDegrees + 360) % 360;

        double angleLimits[] = new double[4];
        angleLimits[0] = Math.atan2(-centerY, fieldWidth - centerX);
        angleLimits[1] = Math.atan2(fieldHeight - centerY, fieldWidth - centerX);
        angleLimits[2] = Math.atan2(fieldHeight - centerY, -centerX);
        angleLimits[3] = Math.atan2(-centerY, -centerX);

        for (int i = 0; i < 4; i++) {
            angleLimits[i] = (Math.toDegrees(angleLimits[i]) + 360) % 360;
        }

        if (angle >= angleLimits[0] || angle <= angleLimits[1]) {
            return SystemSides.Right;
        } else if (angle < angleLimits[2]) {
            return SystemSides.Down;
        } else if (angle <= angleLimits[3]) {
            return SystemSides.Left;
        } else {
            return SystemSides.Up;
        }
    }

    enum SystemSides {Right, Down, Left, Up}

    ;

    public static void swap(Object objectA, Object objectB) {
        Object temp = objectA;
        objectA = objectB;
        objectB = temp;
    }

    protected void deleteBulletsOutsideOfBorders() {
        for (Bullet bullet : bullets) {
            if (collisionChecker.isObjectOutsideOfBoundaries(bullet)) {
                bullets.remove(bullet);
            }
        }
    }

    protected boolean checkWhetherTheGameIsOver() {
        for (Player player : players) {
            if (player.getPlayerTank().getHealthPoints() <= 0) {
                return true;
            }
        }
        return false;
    }

    protected void pauseGame() {
        gameLoopInfiniteTimeline.pause();
    }

    protected void unpauseGame() {
        currentTimeInNanos = System.nanoTime();
        gameLoopInfiniteTimeline.play();
    }

    protected void endGame() {
        gameLoopInfiniteTimeline.stop();
    }
}
