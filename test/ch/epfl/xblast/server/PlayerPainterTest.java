package ch.epfl.xblast.server;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public class PlayerPainterTest {
    @Test
    public void rightVulnPlayerIndexIsChosenForDifferentPlayers() {
        Player vulnE1 = new Player(PlayerID.PLAYER_1,
                Sq.constant(new Player.LifeState(3,Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(0,0), Direction.E)), 2, 2);
        Player vulnE2 = new Player(PlayerID.PLAYER_2,
                Sq.constant(new Player.LifeState(3,Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(0,0), Direction.E)), 2, 2);
        Player vulnE3 = new Player(PlayerID.PLAYER_3,
                Sq.constant(new Player.LifeState(3,Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(0,0), Direction.E)), 2, 2);
        Player vulnE4 = new Player(PlayerID.PLAYER_4,
                Sq.constant(new Player.LifeState(3,Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(0,0), Direction.E)), 2, 2);
        Player vulnW1 = new Player(PlayerID.PLAYER_1,
                Sq.constant(new Player.LifeState(3,Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(0,0), Direction.W)), 2, 2);
        Player vulnS1 = new Player(PlayerID.PLAYER_1,
                Sq.constant(new Player.LifeState(3,Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(0,0), Direction.S)), 2, 2);
        
        
        assertEquals(3, PlayerPainter.byteForPlayer(0, vulnE1));
        assertEquals(23, PlayerPainter.byteForPlayer(0, vulnE2));
        assertEquals(43, PlayerPainter.byteForPlayer(0, vulnE3));
        assertEquals(63, PlayerPainter.byteForPlayer(0, vulnE4));
        assertEquals(9, PlayerPainter.byteForPlayer(0, vulnW1));
        assertEquals(6, PlayerPainter.byteForPlayer(0, vulnS1));
        
    }
    
    @Test
    public void rightInvulnPlayerIndexIsChosenForDifferentPlayers() {
        Player invulnE1 = new Player(PlayerID.PLAYER_1,
                Sq.constant(new Player.LifeState(3,Player.LifeState.State.INVULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(0,0), Direction.E)), 2, 2);
        
        
        assertEquals(3, PlayerPainter.byteForPlayer(0, invulnE1));
        assertEquals(83, PlayerPainter.byteForPlayer(1, invulnE1));
    }
    
    @Test
    public void rightMovingPlayerIndexIsChosenForDifferentPlayers() {
        Player invulnE1Step1 = new Player(PlayerID.PLAYER_1,
                Sq.constant(new Player.LifeState(3,Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(0,0), Direction.E)), 2, 2);
        Player invulnE1Step2 = new Player(PlayerID.PLAYER_1,
                Sq.constant(new Player.LifeState(3,Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(1,0), Direction.E)), 2, 2);
        Player invulnE1Step3 = new Player(PlayerID.PLAYER_1,
                Sq.constant(new Player.LifeState(3,Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(2,0), Direction.E)), 2, 2);
        Player invulnE1Step4 = new Player(PlayerID.PLAYER_1,
                Sq.constant(new Player.LifeState(3,Player.LifeState.State.VULNERABLE)),
                Sq.constant(new Player.DirectedPosition(new SubCell(3,0), Direction.E)), 2, 2);
        
        
        assertEquals(3, PlayerPainter.byteForPlayer(0, invulnE1Step1));
        assertEquals(4, PlayerPainter.byteForPlayer(1, invulnE1Step2));
        assertEquals(3, PlayerPainter.byteForPlayer(2, invulnE1Step3));
        assertEquals(5, PlayerPainter.byteForPlayer(3, invulnE1Step4));
    }
}
