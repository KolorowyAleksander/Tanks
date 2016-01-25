package tanks;

/**
 *
 * @author krzyś
 */
public class KrzysAI extends ArtificialPlayer {

    private final static double customInterval = 0.55;
    private double timeCount=0;
    private double myHp = getPlayerTank().getHealthPoints();
    private boolean huntingBonuses = true;
    private double notSeenSince = 0.0;
    private RoundGameObject target = getPlayerTank();

    private Move.Movement myMovement = Move.Movement.Forward;
    private Move.Rotation myRotation = Move.Rotation.Clockwise;
    private Move.Shooting myShooting;

    public KrzysAI(int playerNumber, Tank tank) {
        super(playerNumber, "Huntekah", tank);
    }

    public double getAngle(RoundGameObject object) {
        double mathAngle = Math.toDegrees(Math.atan2(-getPlayerTank().getCenterY() + object.getCenterY(), -getPlayerTank().getCenterX() + object.getCenterX()));
      ///  System.out.println("atan2 angle "  + mathAngle);
       /* if (mathAngle < 0.0){
            mathAngle += 360.0;
        }
        mathAngle = (mathAngle + 90) % 360;*/
//mathAngle = (getPlayerTank().getRotationAngle()-180.0) - mathAngle;
       // mathAngle+=90.0;
        mathAngle = (mathAngle + 450) % 360;
        mathAngle-=getPlayerTank().getRotationAngle();
      // System.out.println("Angle: "+mathAngle );
        return mathAngle;
    }

    public double getDistance(RoundGameObject object) {
        return Math.sqrt(Math.pow((object.getCenterX() - getPlayerTank().getCenterX()), 2) + Math.pow((object.getCenterY() - getPlayerTank().getCenterY()), 2));
    }

    public double getTrueShootingAngle(RoundGameObject object){
        return Math.toDegrees(Math.atan2(2 * object.getRadius(),getDistance(object)));

    }

    public void whileAiming() {
        huntingBonuses=false;
        for (RoundGameObject object : visibleObjectsBuffer) {
            if (object instanceof Tank) {
                notSeenSince=0.0;
                target = object;
               // System.out.println(target.getCenterX() + " " + target.getCenterY());
                double objectAngle = getAngle(object);
                double objectDistance = getDistance(object);
                if (objectAngle < -getTrueShootingAngle(target) /4) {
                    myRotation = Move.Rotation.CounterClockwise;
                } else if (objectAngle > getTrueShootingAngle(target) /4) {
                    myRotation = Move.Rotation.Clockwise;
                } else {
                    myRotation = Move.Rotation.Staying;
                }

                if (Math.abs(objectAngle) < getTrueShootingAngle(object)) {
                    myShooting = Move.Shooting.Shoots;
                } else {
                    myShooting = Move.Shooting.NotShoots;
                }
                if (objectDistance > 80) {
                    myMovement = Move.Movement.Forward;
                } else if (objectDistance < 75) {
                    myMovement = Move.Movement.Backward;
                }
            }
            if(object instanceof Bonus){

                double objectDistance = getDistance(object);
                if(objectDistance<=400 && notSeenSince>0.3){
                    //System.out.println("BonusHunt");
                    huntingBonuses=true;
                    double objectAngle = getAngle(object);
                    if (objectAngle < -getTrueShootingAngle(target) /4) {
                        myRotation = Move.Rotation.CounterClockwise;
                    } else if (objectAngle > getTrueShootingAngle(target) /4) {
                        myRotation = Move.Rotation.Clockwise;
                    } else {
                        myRotation = Move.Rotation.Staying;
                    }
                     myMovement = Move.Movement.Forward;
                }
            }
        }
        if(huntingBonuses){
            updateHp();
            if(lostHp())huntingBonuses=false;
        }
        if(notSeenSince>0.2 && !huntingBonuses)notSeen();

    }

private boolean lostHp(){
    if(myHp-getPlayerTank().getHealthPoints() >0) return true;
return false;
}
    private void updateHp(){myHp = getPlayerTank().getHealthPoints();}

    public void notSeen(){
        if(notSeenSince < 1.0) {
            if (!target.equals(getPlayerTank())) {
                //System.out.print("Blind ");
                double objectAngle = getAngle(target);
                double objectDistance = getDistance(target);
                if (objectAngle < -getTrueShootingAngle(target) /4) {
                    myRotation = Move.Rotation.CounterClockwise;
                } else if (objectAngle > getTrueShootingAngle(target) /4) {
                    myRotation = Move.Rotation.Clockwise;
                } else {
                    myRotation = Move.Rotation.Staying;
                }

                if (Math.abs(objectAngle) < getTrueShootingAngle(target)) {
                    myShooting = Move.Shooting.Shoots;
                } else {
                    myShooting = Move.Shooting.NotShoots;
                }
                if (objectDistance > 120) {
                    myMovement = Move.Movement.Forward;
                } else if (objectDistance < 100) {
                    myMovement = Move.Movement.Backward;
                }
            }
        }
        else {
            //System.out.println("Looking for");
            if(myRotation.equals(Move.Rotation.Staying))myRotation = Move.Rotation.Clockwise;
        }
    }

    public Move makeMove(double deltaTime) {
        notSeenSince+=deltaTime;
      /*  if(getPlayerTank().getRangeOfVision() < 160.0) {
            getPlayerTank().addVision(5.0);
        }
*/
      /*  timeCount+=deltaTime;
        if(timeCount>0.5) {
            System.out.println("Player Angle " + getPlayerTank().getRotationAngle());
        timeCount=0;
        }*/
        getPlayerTank().addHealthPoints(1.5);
            whileAiming();//myShooting = Move.Shooting.Shoots;

        return new Move(myMovement, myRotation, myShooting);
    }
}
