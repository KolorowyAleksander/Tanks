package tanks;

/**
 * Created by Ahmad on 18.01.2016.
 */
public class AhmadAI extends ArtificialPlayer {

    public AhmadAI(int PlayerNumber, Tank tank) {
        super(PlayerNumber, "Ahmad", tank);
    }

    public double Hp = getPlayerTank().getHealthPoints();
    public Move makeMove(double deltatime) {
        Tank tank;
        tank = getPlayerTank();
        double currenthp = getPlayerTank().getHealthPoints();
        if(currenthp < Hp){
            Hp = currenthp;
            return  new Move(Move.Movement.Staying, Move.Rotation.CounterClockwise, Move.Shooting.Shoots);
        }
        for (RoundGameObject object : visibleObjectsBuffer){
            double odleglosc = Math.sqrt(Math.pow(tank.getCenterX() - object.getCenterX(), 2) + (Math.pow(tank.getCenterY() - object.getCenterY(), 2)));
            if(object instanceof Tank ) {
                if (odleglosc <= 100.0) return new Move(Move.Movement.Backward, Move.Rotation.Staying, Move.Shooting.Shoots);
            }
            if (object instanceof  Tank || object instanceof Bonus){
                double A = Math.sin((tank.getRotationAngle()-90)* Math.PI / 180) * -1;
                double B = Math.cos((tank.getRotationAngle()-90)* Math.PI / 180) ;
                double C = -(tank.getCenterX()* A + tank.getCenterY()* B);

                if((object.getCenterX()*A + object.getCenterY() * B + C) < 0) {
                    return new Move(Move.Movement.Forward, Move.Rotation.CounterClockwise, Move.Shooting.Shoots);
                }
                else if((object.getCenterX()*A + object.getCenterY() * B + C) > 0){
                    return new Move(Move.Movement.Forward, Move.Rotation.Clockwise, Move.Shooting.Shoots);
                } else {
                    return new Move(Move.Movement.Forward, Move.Rotation.Staying, Move.Shooting.Shoots);
                }
            }
        }
        return new Move(Move.Movement.Backward, Move.Rotation.CounterClockwise, Move.Shooting.NotShoots);
    }
}

