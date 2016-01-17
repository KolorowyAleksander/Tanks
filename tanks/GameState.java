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

    protected Player players[];

    protected ConcurrentLinkedQueue<Bullet> bullets;
    protected ConcurrentLinkedQueue<Bonus> bonuses;

    protected EventHandler<ActionEvent> gameLoop;

    private Timeline gameLoopInfiniteTimeline;

    protected long startTimeInNanos;
    protected long currentTimeInNanos;

    final static private int framesPerSecond = 30;

    protected double fieldWidth = 800.0, fieldHeight = 600.0;
    protected double damageOnTanksCollision = 100.0;
    protected static final Point2D startingPositions[] = {new Point2D(200, 300), new Point2D(600, 300)};

    public GameState(String fxmlFileName, String playerOneName, String playerTwoName) {
        super(fxmlFileName);

        gameObjectFactory = new GameObjectFactory();
        collisionChecker = new CollisionChecker(fieldWidth, fieldHeight);

        players = new Player[2];

        bullets = new ConcurrentLinkedQueue<Bullet>();

        controller.setAssociatedState(this);

        gameLoopInfiniteTimeline = createGameLoopTimeline(framesPerSecond);
        gameLoopInfiniteTimeline.play();

        currentTimeInNanos = startTimeInNanos = System.nanoTime();
    }

    private Timeline createGameLoopTimeline(int framesPerSecond) {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame gameLoopFrame = new KeyFrame(Duration.millis(1000.0 / (double)framesPerSecond), createGameLoop());
        timeline.getKeyFrames().add(gameLoopFrame);

        return timeline;
    }

    private EventHandler<ActionEvent> createGameLoop() {
        gameLoop = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                long newTimeInNanos = System.nanoTime();
                double deltaTime = (double)(newTimeInNanos - currentTimeInNanos) / 1000000000.0;
                currentTimeInNanos = newTimeInNanos;
                updateGame(deltaTime);
                draw();
            }
        };

        return gameLoop;
    }

    public Player getPlayer(int playerNumber) {
        return players[playerNumber];
    }

    protected void draw() {
        GameStateController gameStateController = (GameStateController)controller;
        gameStateController.clearCanvas();
        gameStateController.drawBackground();
        gameStateController.drawBorder();
        drawTanks();
        drawBullets();
        drawPlayersData();
    }

    protected void drawPlayersData() {
        ((GameStateController)controller).drawPlayersData(players);
    }

    protected void drawTanks() {
        for (Player player : players) {
            Tank tank = player.getPlayerTank();
            drawGameObjectOnCanvas(tank);
        }
    }

    protected void drawBullets() {
        for (Bullet bullet  : bullets) {
            drawGameObjectOnCanvas(bullet);
        }
    }

    protected void drawGameObjectOnCanvas(RoundGameObject gameObject) {
        ((GameStateController)(controller)).drawRotatedImageOnGameCanvas(gameObject.getImage(), gameObject.getCenterX(),
                gameObject.getCenterY(), gameObject.getRotationAngle());
    }

    protected void updateGame(double deltaTime) {
        updateTanks(deltaTime);
        updateBullets(deltaTime);
        checkCollisions();
        deleteBulletsOutsideOfBorders();
    }

    protected void updateTanks(double deltaTime) {
        for (Player player : players) {
            Move playerMove = player.makeMove();
            Tank playerTank = player.getPlayerTank();
            System.out.println("Player #" + player.getPlayerNumber() + ", HP = " + playerTank.getHealthPoints());

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


    protected void checkCollisions() {
        ConcurrentLinkedQueue<RoundGameObject> gameObjects = new ConcurrentLinkedQueue<RoundGameObject>();
        gameObjects.addAll(bullets);
        for (Player player : players) {
            gameObjects.add(player.getPlayerTank());
        }

        for (RoundGameObject gameObjectA : gameObjects) {
            for (RoundGameObject gameObjectB : gameObjects) {
                if (gameObjectA != gameObjectB) {
                    System.out.println(gameObjectA.getClass().toString() + " checking collision with " + gameObjectB.getClass().toString());
                    if (collisionChecker.isColliding(gameObjectA, gameObjectB)) {
                        System.out.println(gameObjectA.getClass().toString() + " collided with " + gameObjectB.getClass().toString());
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
            }
            else {
                gameObjectA.moveTo(shiftPointOnTankCollision(centerA, center, gameObjectA.getRadius()));
            }

            if (collisionChecker.isCircleInsideOfBoundaries(new Circle(newCenterB.getX(), newCenterB.getY(), gameObjectB.getRadius()))) {
                gameObjectB.moveTo(newCenterB);
            }
            else {
                gameObjectB.moveTo(shiftPointOnTankCollision(centerB, center, gameObjectB.getRadius()));
            }
        }
        else if (gameObjectA instanceof Tank && gameObjectB instanceof Bullet) {
            ((Tank) gameObjectA).addHealthPoints(-((Bullet) gameObjectB).getDamage());
            bullets.remove(gameObjectB);
        }
        else if (gameObjectA instanceof Tank && gameObjectB instanceof Bonus) {

        }
        else if (gameObjectA instanceof Bullet && gameObjectB instanceof Bullet) {
            return;
        }
        else if (gameObjectA instanceof Bullet && gameObjectB instanceof Bonus) {
            return;
        }
        else if (gameObjectA instanceof Bonus && gameObjectB instanceof Bonus) {
            return;
        }
    }

    protected Point2D shiftPointOnTankCollision(Point2D currentPosition, Point2D center, double radius) {
        double posX = currentPosition.getX(), posY = currentPosition.getY();
        double centerX = center.getX(), centerY = center.getY();

        double a = (posY - centerY) / (posX - centerX);
        double b = posY - a * posX;

        Point2D possibleShiftPoints[] = new Point2D[4];

        possibleShiftPoints[0] = new Point2D(-b / a , 0);
        possibleShiftPoints[1] = new Point2D(fieldWidth, a*fieldWidth + b);
        possibleShiftPoints[2] = new Point2D((fieldHeight - b) / a, fieldHeight);
        possibleShiftPoints[3] = new Point2D(0, b);

        double distances[] = new double[4];
        double minimumDistance = Double.MAX_VALUE;
        int minimumIndex = 0;
        for (int i = 0; i < 4; i++) {
            distances[i] = currentPosition.distance(possibleShiftPoints[i]);
            if (distances[i] < minimumDistance) {
                minimumDistance = distances[i];
                minimumIndex = i;
            }
        }

        Point2D crossPoint = possibleShiftPoints[minimumIndex];

        double newCenterX = 0, newCenterY = 0;

        switch(minimumIndex) {
            case 0:
                newCenterX = crossPoint.getX() + (radius / a);
                newCenterY = radius;
                break;

            case 1:
                newCenterX = fieldWidth - radius;
                newCenterY = crossPoint.getY() - (radius * a);
                break;

            case 2:
                newCenterX = crossPoint.getX() - (radius / a);
                newCenterY = fieldHeight - radius;
                break;

            case 3:
                newCenterX = radius;
                newCenterY = crossPoint.getY() + (radius / a);
        }

        return new Point2D(newCenterX, newCenterY);
    }

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
        System.out.println(bullets.size());
    }

    public long getStartTimeInNanos() {
        return startTimeInNanos;
    }
}
