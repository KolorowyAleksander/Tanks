package tanks;

/**
 *
 * @author krzyś
 */
public class KrzysAI extends ArtificialPlayer {

    private final static double customInterval = 0.55;
    private double customTime = 0.0;
    private int movementOption = 0;
    private boolean lastDirection = false;
    private boolean lastRotation = false;

    private Move.Movement myMovement = Move.Movement.Forward;
    private Move.Rotation myRotation = Move.Rotation.Clockwise;
    private Move.Shooting myShooting;

    public KrzysAI(int playerNumber, Tank tank) {
        super(playerNumber, "Huntekah", tank);
    }

    public double getAngle(RoundGameObject object) {
        double mathAngle = Math.toDegrees(Math.atan2(getPlayerTank().getCenterY() - object.getCenterY(), getPlayerTank().getCenterX() - object.getCenterX()));
        if (mathAngle < 0.0) {
            mathAngle += 360.0;
        }
        mathAngle = (mathAngle + 90) % 360;
        return (getPlayerTank().getRotationAngle() - mathAngle);
    }

    public double getDistance(RoundGameObject object) {
        return Math.sqrt(Math.pow((object.getCenterX() - getPlayerTank().getCenterX()), 2) + Math.pow((object.getCenterY() - getPlayerTank().getCenterY()), 2));
    }

    public void whileAiming() {
        if (lastRotation) {
            myRotation = Move.Rotation.Clockwise;
        } else {
            myRotation = Move.Rotation.CounterClockwise;
        }
        if (lastDirection) {
            myMovement = Move.Movement.Forward;
        } else {
            myMovement = Move.Movement.Backward;
        }
        for (RoundGameObject object : visibleObjectsBuffer) {
            if (object instanceof Tank) {
                double objectAngle = getAngle(object);
                double objectDistance = getDistance(object);

                if (objectAngle > 1.0 && objectAngle < 180.0) {
                    myRotation = Move.Rotation.CounterClockwise;
                    lastRotation = false;
                } else if (objectAngle < 358.0) {
                    myRotation = Move.Rotation.Clockwise;
                    lastRotation = true;
                } else {
                    myRotation = Move.Rotation.Staying;
                }

                if (Math.abs(objectAngle) < 8.0) {
                    myShooting = Move.Shooting.Shoots;
                } else {
                    myShooting = Move.Shooting.NotShoots;
                }
                if (objectDistance > 220) {
                    myMovement = Move.Movement.Forward;
                    lastDirection = true;
                } else if (objectDistance < 90) {
                    myMovement = Move.Movement.Backward;
                    lastDirection = false;
                } else {
                    if (lastDirection) {
                        myMovement = Move.Movement.Forward;
                    } else {
                        myMovement = Move.Movement.Backward;
                    }
                }

            }
        }
    }


public void smartEvadee() {
    myShooting = Move.Shooting.NotShoots;
    if(Math.random()>0.25){
     if (lastRotation) {
                myRotation = Move.Rotation.Clockwise;
            } else {
                myRotation = Move.Rotation.CounterClockwise;
            }
            if (lastDirection) {
                myMovement = Move.Movement.Forward;
            } else {
                myMovement = Move.Movement.Backward;
            }
    }
    else
    {
    myRotation = Move.Rotation.values()[(int)(Math.random() * Move.Rotation.values().length)];
    myMovement = Move.Movement.values()[(int)(Math.random() * Move.Movement.values().length)];
    }
}

    public Move makeMove(double deltaTime) {
        customTime += deltaTime;
        if (getPlayerTank().isReadyToShoot()) {
            whileAiming();//myShooting = Move.Shooting.Shoots;
        
        } else {
            smartEvadee();//myShooting = Move.Shooting.NotShoots
        }
        return new Move(myMovement, myRotation, myShooting);
    }
}
