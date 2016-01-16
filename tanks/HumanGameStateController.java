package tanks;

import javafx.animation.Timeline;
import javafx.beans.binding.BooleanExpression;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;
import java.util.Map;

public class HumanGameStateController extends GameStateController {
    private class KeyboardKeyBinding {
        int playerNumber;
        Move.MoveSections moveSection;
        Enum value;

        KeyboardKeyBinding(int playerNumber, Move.MoveSections section, Enum value) {
            this.playerNumber = playerNumber;
            this.moveSection = section;
            this.value = value;
        }

        public String toString() {
            return "Player #" + playerNumber + ", move section: " + moveSection + ", value: " + value;
        }
    }

    Map<KeyCode, Boolean> isKeyPressedHashMap;

    private Map<KeyCode, KeyboardKeyBinding> keyboardKeyBindingMap;

    public HumanGameStateController() {
        super();
        initializeKeyboardKeyBindingMap();
        initializeIsKeyPressedHashMap();
    }

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

    private void initializeIsKeyPressedHashMap() {
        isKeyPressedHashMap = new HashMap<KeyCode, Boolean>();

        isKeyPressedHashMap.put(KeyCode.UP, false);
        isKeyPressedHashMap.put(KeyCode.DOWN, false);
        isKeyPressedHashMap.put(KeyCode.LEFT, false);
        isKeyPressedHashMap.put(KeyCode.RIGHT, false);
        isKeyPressedHashMap.put(KeyCode.NUMPAD2, false);

        isKeyPressedHashMap.put(KeyCode.W, false);
        isKeyPressedHashMap.put(KeyCode.S, false);
        isKeyPressedHashMap.put(KeyCode.A, false);
        isKeyPressedHashMap.put(KeyCode.D, false);
        isKeyPressedHashMap.put(KeyCode.V, false);
    }

    private final EventHandler<KeyEvent> keyPressedEventHandler = new EventHandler<KeyEvent> () {
        public void handle(final KeyEvent keyEvent) {
            searchForKeyCodeAndSet(keyEvent, true);
        }
    };

    private final EventHandler<KeyEvent> keyReleasedEventHandler = new EventHandler<KeyEvent> () {
        public void handle(final KeyEvent keyEvent) {
            searchForKeyCodeAndSet(keyEvent, false);
        }
    };

    private void searchForKeyCodeAndSet(KeyEvent keyEvent, Boolean bool) {
        KeyCode keyCode = keyEvent.getCode();
        System.out.println(keyCode + " pressed/released");
        if (isKeyPressedHashMap.containsKey(keyCode)) {
            isKeyPressedHashMap.put(keyCode, bool);
        }

    }

    public void setMovesBasedOnKeyboard() {
        KeyboardKeyBinding keyboardKeyBinding;
        KeyCode keyCode;
        HumanGameState humanGameState = (HumanGameState)associatedState;
        for (Map.Entry<KeyCode, Boolean> entry : isKeyPressedHashMap.entrySet()) {
            if (entry.getValue() == true) {
                keyCode = entry.getKey();
                keyboardKeyBinding = keyboardKeyBindingMap.get(keyCode);
                HumanPlayer player = (HumanPlayer)humanGameState.getPlayer(keyboardKeyBinding.playerNumber);
                player.setMoveBufferSection(keyboardKeyBinding.moveSection, keyboardKeyBinding.value);
            }
        }
    }

    @FXML
    void initialize() {
        super.initialize();
        root.setOnKeyPressed(keyPressedEventHandler);
        root.setOnKeyReleased(keyReleasedEventHandler);
    }
}
