package ch.epfl.xblast.server;

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Player.LifeState.State;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class PlayerPainter {
    private final static byte STEP_FOR_PLAYER = 20;
    private final static byte STEP_FOR_DIRECTION = 3;
    private final static byte STEP_FOR_FIRST = 1;
    private final static byte STEP_FOR_SECOND = 2;
    private final static byte BYTE_FOR_DEAD = 100;
    
    private PlayerPainter() {
    }

    public static byte byteForPlayer(int tick, Player player){
        if(!player.isAlive()){
            return BYTE_FOR_DEAD;
        }
        byte bFP = 0;
        if(tick % 2 == 0 && player.lifeState().state() == State.INVULNERABLE){
            bFP = 80;
        } else {
            bFP += player.id().ordinal() * STEP_FOR_PLAYER;
        }
        
        bFP += player.direction().ordinal() * STEP_FOR_DIRECTION;
        
        int mod = (player.direction() == Direction.E || player.direction() == Direction.W)? player.position().x() % 4 : player.position().y() % 4;
        
        if(mod == 1){
            bFP += STEP_FOR_FIRST;
        } else if(mod == 3){
            bFP += STEP_FOR_SECOND;
        }
        
        return bFP;
    }
}
