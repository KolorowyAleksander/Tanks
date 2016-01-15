package tanks;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class GameStateController extends Controller {
    private GameState gameState;

    @FXML
    private Canvas gameCanvas;

    final private double canvasBorderWidth = 10;

    @FXML
    private AnchorPane anchorPane;

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

    public GameStateController() {
        gameState = (GameState)associatedState;
        initializeKeyboardKeyBindingMap();
    }

    @FXML
    void initialize() {
        root.setOnKeyPressed(keyEventHandler);
        gameCanvas.setHeight(600);
        gameCanvas.setWidth(800);
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.setLineWidth(10);
        gc.strokeRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        gameCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(System.nanoTime() - ((GameState) associatedState).getStartTimeInNanos());
            }
        });
    }
}
