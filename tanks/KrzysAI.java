package tanks;
/**
 *
 * @author krzyś
 */
public class KrzysAI extends ArtificialPlayer{
    private final static double customInterval = 0.55;
    private double customTime = 0.0;
    private int movementOption = 0;

    private Move.Movement myMovement = Move.Movement.Forward;
    private Move.Rotation myRotation = Move.Rotation.Clockwise;
    private Move.Shooting myShooting;
    
    public KrzysAI (int playerNumber, String playerName, Tank tank) {
        super(playerNumber, playerName, tank);
    }

    public static int randomOption(){
    return (int)(Math.random() *3);
    }
    
    public Move makeMove(double deltaTime) {
        customTime += deltaTime;
        if(getPlayerTank().isReadyToShoot())myShooting = Move.Shooting.Shoots;
        else myShooting = Move.Shooting.NotShoots;
        
        if(customTime>deltaTime) movementOption = randomOption();
        if(movementOption == 0){
           myMovement = Move.Movement.Forward;
           myRotation = Move.Rotation.Clockwise;
        }
        else if(movementOption ==1){
           myMovement = Move.Movement.Forward;
           myRotation = Move.Rotation.CounterClockwise; 
        }
        else if(movementOption ==2){
            myMovement = Move.Movement.Backward;
           myRotation = Move.Rotation.Staying;
        }
        else {
            myMovement = Move.Movement.Staying;
           myRotation = Move.Rotation.Clockwise;
        }
        return new Move(myMovement, myRotation, myShooting);
    }
}
