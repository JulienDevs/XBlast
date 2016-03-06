package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;

/**
 * This test tests the class Bomb.
 * 
 * @author Maire Cedric (259314)
 * @author Délèze Benjamin (259992)
 *
 */

public class BombTest {
    
    @SuppressWarnings("unused")
    @Test (expected = NullPointerException.class)
    public void bombConstructorFailOwnerID() {
        Bomb bomb = new Bomb(null, new Cell(0, 0), Ticks.BOMB_FUSE_TICKS, 5);
    }
    
    @SuppressWarnings("unused")
    @Test (expected = NullPointerException.class)
    public void bombConstructorFailPosition() {
        Bomb bomb = new Bomb(PlayerID.PLAYER_1, null, Ticks.BOMB_FUSE_TICKS, 5);
    }
    
    @SuppressWarnings("unused")
    @Test (expected = NullPointerException.class)
    public void bombConstructorFailFuseLengths() {
        Bomb bomb = new Bomb(PlayerID.PLAYER_1, new Cell(0, 0), null, 5);
    }
    
    @SuppressWarnings("unused")
    @Test (expected = IllegalArgumentException.class)
    public void bombConstructorFailFuseLength() {
        Bomb bomb = new Bomb(PlayerID.PLAYER_1, new Cell(0, 0), 0, 5);
    }
    
    @SuppressWarnings("unused")
    @Test (expected = IllegalArgumentException.class)
    public void bombConstructorFailRange() {
        Bomb bomb = new Bomb(PlayerID.PLAYER_1, new Cell(0, 0), Ticks.BOMB_FUSE_TICKS, -1);
    }
    
    @SuppressWarnings("unused")
    @Test
    public void bombConstructorAchieve() {
        Bomb bomb = new Bomb(PlayerID.PLAYER_1, new Cell(0, 0), Ticks.BOMB_FUSE_TICKS, 5);
    }
    
    @Test
    public void ownerIdGetterTest() {
        Random random = new Random();
        int randomNumber = random.nextInt(4);
        PlayerID[] tabOfPlayerID = {PlayerID.PLAYER_1, PlayerID.PLAYER_2, PlayerID.PLAYER_3, PlayerID.PLAYER_4};
        
        assertEquals(tabOfPlayerID[randomNumber], (new Bomb(tabOfPlayerID[randomNumber], new Cell(0, 0), Ticks.BOMB_FUSE_TICKS, 5)).ownerId());
    }
    
    @Test
    public void positionGetterTest() {
        Random random = new Random();
        int randomNumber0 = random.nextInt(15);
        int randomNumber1 = random.nextInt(13);
        
        assertEquals(new Cell(randomNumber0, randomNumber1), (new Bomb(PlayerID.PLAYER_1, new Cell(randomNumber0, randomNumber1), Ticks.BOMB_FUSE_TICKS, 5)).position());
    }
    
    @Test
    public void fuseLength_s_GetterTest() {
        if (Sq.iterate(Ticks.BOMB_FUSE_TICKS, u -> u - 1).limit(Ticks.BOMB_FUSE_TICKS).head() == (new Bomb(PlayerID.PLAYER_1, new Cell(0, 0), Ticks.BOMB_FUSE_TICKS, 5).fuseLength())) {
            assertTrue(true);
        }
        else {
            assertTrue(false);
        }
    }
    
    @Test
    public void rangeGetterTest() {
        Random random = new Random();
        int randomNumber = random.nextInt(10);
        
        if (randomNumber == (new Bomb(PlayerID.PLAYER_1, new Cell(0, 0), Ticks.BOMB_FUSE_TICKS, randomNumber).range())) {
            assertTrue(true);
        }
        else {
            assertTrue(false);
        }
    }
    
    @Test
    public void explosionTest() {
        Bomb b = new Bomb(PlayerID.PLAYER_1, new Cell(0, 0), 3, 4);
        
        List<Sq<Sq<Cell>>> expectedExplosion= new ArrayList<Sq<Sq<Cell>>>();
        
        expectedExplosion.add(Sq.repeat(Ticks.EXPLOSION_TICKS, Sq.iterate(b.position(), u -> u.neighbor(Direction.N)).limit(b.range())));
        expectedExplosion.add(Sq.repeat(Ticks.EXPLOSION_TICKS, Sq.iterate(b.position(), u -> u.neighbor(Direction.E)).limit(b.range())));
        expectedExplosion.add(Sq.repeat(Ticks.EXPLOSION_TICKS, Sq.iterate(b.position(), u -> u.neighbor(Direction.S)).limit(b.range())));
        expectedExplosion.add(Sq.repeat(Ticks.EXPLOSION_TICKS, Sq.iterate(b.position(), u -> u.neighbor(Direction.W)).limit(b.range())));
        
        List<Sq<Sq<Cell>>> temporaryExplosion = b.explosion();
        
        for (int i = 0; i < expectedExplosion.size(); i++) {
            if (temporaryExplosion.get(i).head().head() == expectedExplosion.get(i).head().head()) {
                assertTrue(false);
            }
            if (temporaryExplosion.get(i).head().tail().tail().tail().head() == expectedExplosion.get(i).head().tail().tail().tail().head()) {
                assertTrue(false);
            }
            if (temporaryExplosion.get(i).tail().tail().head().tail().head() == expectedExplosion.get(i).tail().tail().head().tail().head()) {
                assertTrue(false);
            }
        }
        
        assertTrue(true);
    }
    
    @Test (expected = NoSuchElementException.class)
    public void explosionArmTowardsTestFail() {
        Bomb bomb = new Bomb(PlayerID.PLAYER_1, new Cell(2, 2), Ticks.BOMB_FUSE_TICKS, 2);
        
        bomb.explosionArmTowards(Direction.S).head().tail().tail().head();
    }
}