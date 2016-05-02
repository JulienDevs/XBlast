/**
 * 
 */
package ch.epfl.xblast.client.debug;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.client.KeyboardEventHandler;
import ch.epfl.xblast.client.XBlastComponent;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;
import ch.epfl.xblast.server.debug.RandomEventGenerator;

/**
 * @author Yaron
 *
 */
/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public class RandomGame {
    public static void main(String[] args) {
        XBlastComponent xbc = new XBlastComponent();
        Map<Integer, PlayerAction> kb = new HashMap<>();
        kb.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        kb.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        kb.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        kb.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        kb.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        kb.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        Consumer<PlayerAction> c = x -> {
            if (x != null) {
                System.out.println(x.ordinal());
            }
        };
        xbc.addKeyListener(new KeyboardEventHandler(kb, c));
        JFrame frames = new JFrame("XBC");
        frames.setContentPane(xbc);
        frames.setUndecorated(true);
        frames.setResizable(false);
        frames.setVisible(true);
        frames.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frames.pack();
        frames.setLocationRelativeTo(null);
        xbc.requestFocusInWindow();
        Level level = Level.DEFAULT_LEVEL;
        GameState gameState = level.gameState();
        RandomEventGenerator reg = new RandomEventGenerator(1, 15, 100);
        while (!gameState.isGameOver()) {
            try {
                Thread.sleep(50L);
            } catch (Exception e) {
            }
            xbc.setGameState(GameStateDeserializer.deserializeGameState(
                    GameStateSerializer.serialize(level.boardPainter(),
                            gameState)),
                    PlayerID.PLAYER_1);
            gameState = gameState.next(reg.randomSpeedChangeEvents(),
                    reg.randomBombDropEvents());

        }
        xbc.setGameState(
                GameStateDeserializer.deserializeGameState(GameStateSerializer
                        .serialize(level.boardPainter(), gameState)),
                PlayerID.PLAYER_1);

    }
}
