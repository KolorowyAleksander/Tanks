package tanks;

public class HumanGameState extends GameState {


    public HumanGameState(String fxmlFileName) {
        super(fxmlFileName);

        players = new HumanPlayer[2];
    }
}
