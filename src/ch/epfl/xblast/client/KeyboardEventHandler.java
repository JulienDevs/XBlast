package ch.epfl.xblast.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import ch.epfl.xblast.PlayerAction;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 * 
 * Represent a keyboard events auditor
 */
public class KeyboardEventHandler implements KeyListener {

    private final Map<Integer, PlayerAction> actionsMap;
    private final Consumer<PlayerAction> actionsConsumer;

    
    /**
     * Main constructor
     * @param actionsMap
     * @param actionsConsumer
     */
    
    public KeyboardEventHandler(Map<Integer, PlayerAction> actionsMap,
            Consumer<PlayerAction> actionsConsumer) {
        this.actionsMap = Collections.unmodifiableMap(
                new HashMap<Integer, PlayerAction>(actionsMap));
        this.actionsConsumer = actionsConsumer;

    }

    
    
    
    /**
     * 
     */
    @Override 
    public void keyPressed(KeyEvent event) {
        int code = event.getKeyCode();
        if(actionsMap.containsKey(code)){
            actionsConsumer.accept(actionsMap.get(code));
        }
        
        

    }
    @Override
    public void keyReleased(KeyEvent event) {
    }
    @Override
    public void keyTyped(KeyEvent event) {
    }

}
