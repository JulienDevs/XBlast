package ch.epfl.xblast.client.debug;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import javax.swing.JFrame;

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.client.KeyboardEventHandler;
import ch.epfl.xblast.client.XBlastComponent;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;
import ch.epfl.xblast.server.debug.RandomEventGenerator;

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
        Set<PlayerAction> set = new HashSet<>();
        Consumer<PlayerAction> c = x -> {
            set.add(x);
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
        ch.epfl.xblast.server.GameState gameState = Level.DEFAULT_LEVEL.gameState();
        RandomEventGenerator reg = new RandomEventGenerator(1, 15, 100);
        while (!gameState.isGameOver()) {
            xbc.setGameState(GameStateDeserializer.deserializeGameState(
                    GameStateSerializer.serialize(level.boardPainter(),
                            gameState)),
                    PlayerID.PLAYER_1);
            gameState = gameState.next(map(set),
                    set(set));
            set.clear();
            
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        xbc.setGameState(
                GameStateDeserializer.deserializeGameState(GameStateSerializer
                        .serialize(level.boardPainter(), gameState)),
                PlayerID.PLAYER_1);
    }
    
    public static Map<PlayerID, Optional<Direction>> map(Set<PlayerAction> set){
        Map<PlayerID, Optional<Direction>> map = new HashMap<>();
        for(PlayerAction pA : set){
            if(pA == PlayerAction.MOVE_N){
                map.put(PlayerID.PLAYER_1, Optional.of(Direction.N));
            }
            if(pA == PlayerAction.MOVE_E){
                map.put(PlayerID.PLAYER_1, Optional.of(Direction.E));
            }
            if(pA == PlayerAction.MOVE_S){
                map.put(PlayerID.PLAYER_1, Optional.of(Direction.S));
            }
            if(pA == PlayerAction.MOVE_W){
                map.put(PlayerID.PLAYER_1, Optional.of(Direction.W));
            }
            if(pA == PlayerAction.STOP){
                map.put(PlayerID.PLAYER_1, Optional.empty());
            }
        }
        return map;
    }
    
    public static Set<PlayerID> set(Set<PlayerAction> set){
        Set<PlayerID> set1 = new HashSet<>();
        for(PlayerAction pA : set){
            if(pA == PlayerAction.DROP_BOMB){
                set1.add(PlayerID.PLAYER_1);
            }
        }
        return set1;
    }
}