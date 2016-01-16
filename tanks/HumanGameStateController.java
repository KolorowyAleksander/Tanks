package tanks;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.Map;

public class HumanGameStateController extends GameStateController {
    private HumanGameState humanGameState;

    public HumanGameStateController() {
        super();
        humanGameState = (HumanGameState) gameState;
        initializeKeyboardKeyBindingMap();
    }

    private class KeyboardKeyBinding {
        int playerNumber;
        Move.MoveSections moveSection;
        Enum value;

        KeyboardKeyBinding(int playerNumber, Move.MoveSections section, Enum value) {
            this.playerNumber = playerNumber;
            this.moveSection = section;
            this.value = value;
        }
    }

    private Map<KeyCode, KeyboardKeyBinding> keyboardKeyBindingMap;

    private void initializeKeyboardKeyBindingMap() {
        keyboardKeyBindingMap = new HashMap<KeyCode, KeyboardKeyBinding>();

        keyboardKeyBindingMap.put(KeyCode.UP, new KeyboardKeyBinding(0, Move.MoveSections.Movement, Move.Movement.Forward));
        keyboardKeyBindingMap.put(KeyCode.DOWN, new KeyboardKeyBinding(0, Move.MoveSections.Movement, Move.Movement.Backward));
        keyboardKeyBindingMap.put(KeyCode.LEFT, new KeyboardKeyBinding(0, Move.MoveSections.Rotation, Move.Rotation.CounterClockwise));
        keyboardKeyBindingMap.put(KeyCode.RIGHT, new KeyboardKeyBinding(0, Move.MoveSections.Rotation, Move.Rotation.Clockwise));
        keyboardKeyBindingMap.put(KeyCode.NUMPAD2, new KeyboardKeyBinding(0, Move.MoveSections.Shooting, Move.Shooting.Shoots));

        keyboardKeyBindingMap.put(KeyCode.W, new KeyboardKeyBinding(1, Move.MoveSections.Movement, Move.Movement.Forward));
        keyboardKeyBindingMap.put(KeyCode.S, new KeyboardKeyBinding(1, Move.MoveSections.Movement, Move.Movement.Backward));
        keyboardKeyBindingMap.put(KeyCode.A, new KeyboardKeyBinding(1, Move.MoveSections.Rotation, Move.Rotation.CounterClockwise));
        keyboardKeyBindingMap.put(KeyCode.D, new KeyboardKeyBinding(1, Move.MoveSections.Rotation, Move.Rotation.Clockwise));
        keyboardKeyBindingMap.put(KeyCode.V, new KeyboardKeyBinding(1, Move.MoveSections.Shooting, Move.Shooting.Shoots));
    }

    private final EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent> () {
        public void handle(final KeyEvent keyEvent) {
            KeyboardKeyBinding keyboardKeyBinding = keyboardKeyBindingMap.get(keyEvent);
            HumanPlayer player = (HumanPlayer) gameState.getPlayer(keyboardKeyBinding.playerNumber);
            player.setMoveBufferSection(keyboardKeyBinding.moveSection, keyboardKeyBinding.value);
        }
    };

    @FXML
    void initialize() {
        super.initialize();
        root.setOnKeyPressed(keyEventHandler);
    }
}
