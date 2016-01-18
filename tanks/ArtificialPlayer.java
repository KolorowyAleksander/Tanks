package tanks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class ArtificialPlayer extends Player {
    private List<RoundGameObject> visibleObjectsBuffer;

    public ArtificialPlayer(int playerNumber, String playerName, Tank tank) {
        super(playerNumber, playerName, tank);
        visibleObjectsBuffer = new ArrayList<RoundGameObject>();
    }

    public abstract Move makeMove();

    public void receiveVisibleObjectsData(List<RoundGameObject> visibleObjects) {
        visibleObjectsBuffer = visibleObjects;
    }
}
