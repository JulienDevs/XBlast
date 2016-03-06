package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import java.util.Objects;
import java.util.Random;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.DirectedPosition;
import ch.epfl.xblast.server.Player.LifeState;
import ch.epfl.xblast.server.Player.LifeState.State;

/**
 * This test tests the class Player.
 * 
 * @author Maire Cedric (259314)
 * @author Délèze Benjamin (259992)
 *
 */

public class PlayerTest {
    
    @SuppressWarnings("unused")
    @Test (expected = NullPointerException.class)
    public void playerConstructorFailOwnerID() {
        Player player = new Player(null, 3, new Cell(0, 0), 5, 25);
    }
    
    @SuppressWarnings("unused")
    @Test (expected = NullPointerException.class)
    public void playerConstructorFailLifeStates() {
        Player player = new Player(PlayerID.PLAYER_1, null, DirectedPosition.stopped(new DirectedPosition(SubCell.centralSubCellOf(Objects.requireNonNull(new Cell(0, 0))), Direction.S)), 5, 25);
    }
    
    @SuppressWarnings("unused")
    @Test (expected = NullPointerException.class)
    public void playerConstructorFailDirectedPosition() {
        Player player = new Player(PlayerID.PLAYER_1, Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS, new LifeState(ArgumentChecker.requireNonNegative(3), State.INVULNERABLE)).concat(Sq.constant(new LifeState(3, State.VULNERABLE))), null, 5, 25);
    }
    
    @SuppressWarnings("unused")
    @Test (expected = IllegalArgumentException.class)
    public void playerConstructorFailMaxBomb() {
        Player player = new Player(PlayerID.PLAYER_1, 3, new Cell(0, 0), -1, 25);
    }
    
    @SuppressWarnings("unused")
    @Test (expected = IllegalArgumentException.class)
    public void playerConstructorFailRange() {
        Player player = new Player(PlayerID.PLAYER_1, 3, new Cell(0, 0), 5, -1);
    }
    
    @SuppressWarnings("unused")
    @Test (expected = IllegalArgumentException.class)
    public void playerConstructorFailLives() {
        Player player = new Player(PlayerID.PLAYER_1, -1, new Cell(0, 0), 5, 25);
    }
    
    @SuppressWarnings("unused")
    @Test (expected = NullPointerException.class)
    public void playerConstructorFailPosition() {
        Player player = new Player(PlayerID.PLAYER_1, 3, null, 5, 25);
    }
    
    @SuppressWarnings("unused")
    @Test
    public void playerConstructorsAchieve() {
        Player player0 = new Player(PlayerID.PLAYER_1, Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS, new LifeState(ArgumentChecker.requireNonNegative(3), State.INVULNERABLE)).concat(Sq.constant(new LifeState(3, State.VULNERABLE))), DirectedPosition.stopped(new DirectedPosition(SubCell.centralSubCellOf(Objects.requireNonNull(new Cell(0, 0))), Direction.S)), 5, 25);
        Player player1 = new Player(PlayerID.PLAYER_2, 3, new Cell(0, 0), 5, 25);
    }
    
    @Test
    public void ownerIdGetterTest() {
        Random random = new Random();
        int randomNumber = random.nextInt(4);
        PlayerID[] tabOfPlayerID = {PlayerID.PLAYER_1, PlayerID.PLAYER_2, PlayerID.PLAYER_3, PlayerID.PLAYER_4};
        
        if (tabOfPlayerID[randomNumber] == (new Player(tabOfPlayerID[randomNumber], 3, new Cell(0, 0), 5, 25)).id()) {
            assertTrue(true);
        }
        else {
            assertTrue(false);
        }
    }
    
    @Test
    public void lifeStateGetterTest() {
        Player player = new Player(PlayerID.PLAYER_2, 3, new Cell(0, 0), 5, 25);
        
        if (player.lifeState().lives() == 3 && player.lifeState().state() == Player.LifeState.State.INVULNERABLE) {
            assertTrue(true);
        }
        else {
            assertTrue(false);
        }
    }
    
    @Test
    public void livesGetterTest() {
        Random random = new Random();
        int randomNumber = Math.abs(random.nextInt());
        Player player = new Player(PlayerID.PLAYER_2, randomNumber, new Cell(0, 0), 5, 25);
        
        assertEquals(randomNumber, player.lives());
    }
    
    @Test
    public void isAliveTest() {
        Player player0 = new Player(PlayerID.PLAYER_2, 3, new Cell(0, 0), 5, 25);
        Player player1 = new Player(PlayerID.PLAYER_2, 0, new Cell(0, 0), 5, 25);
        
        if (player0.isAlive() && !player1.isAlive()) {
            assertTrue(true);
        }
        else {
            assertTrue(false);
        }
    }
    
    @Test
    public void directredPositionsPositionAndDirectionTest() {
        Player player = new Player(PlayerID.PLAYER_2, 3, new Cell(0, 0), 5, 25);
        
        if (SubCell.centralSubCellOf(new Cell(0, 0)).equals(player.position()) && Direction.S == player.direction()) {
            assertTrue(true);
        }
        else {
            assertTrue(false);
        }
    }
    
    @Test
    public void maxBombsTest() {
        Random random = new Random();
        int randomNumber = Math.abs(random.nextInt());
        Player player = new Player(PlayerID.PLAYER_2, 3, new Cell(0, 0), randomNumber, 25);
        
        assertEquals(randomNumber, player.maxBombs());
    }
    
    @Test
    public void withMaxBombsTest() {
        Random random = new Random();
        int randomNumber = Math.abs(random.nextInt());
        Player player = new Player(PlayerID.PLAYER_2, 3, new Cell(0, 0), randomNumber, 25);
        player = player.withMaxBombs(3);
        
        assertEquals(3, player.maxBombs());
    }
    
    @Test
    public void rangeGetterTest() {
        Random random = new Random();
        int randomNumber = Math.abs(random.nextInt());
        Player player = new Player(PlayerID.PLAYER_2, 3, new Cell(0, 0), 3, randomNumber);
        
        assertEquals(randomNumber, player.bombRange());
    }
    
    @Test
    public void withBombRangeTest() {
        Random random = new Random();
        int randomNumber = Math.abs(random.nextInt());
        Player player = new Player(PlayerID.PLAYER_2, 3, new Cell(0, 0), 3, randomNumber);
        player = player.withBombRange(25);
        
        assertEquals(25, player.bombRange());
    }
    
    @Test
    public void newBombTest() {
        Player player = new Player(PlayerID.PLAYER_2, 3, new Cell(0, 0), 3, 25);
        player.newBomb();
    }
    
    @SuppressWarnings("unused")
    @Test (expected = IllegalArgumentException.class)
    public void lifeStateConstructorFailLives() {
        LifeState lifeState = new LifeState(-1, Player.LifeState.State.VULNERABLE);
    }
    
    @SuppressWarnings("unused")
    @Test (expected = NullPointerException.class)
    public void lifeStateConstructorFailState() {
        LifeState lifeState = new LifeState(3, null);
    }
    
    @SuppressWarnings("unused")
    @Test
    public void lifeStateConstructorAchieve() {
        LifeState lifeState = new LifeState(3, Player.LifeState.State.VULNERABLE);
    }
    
    @Test
    public void lifeStateAllGettersTest() {
        LifeState lifeState = new LifeState(3, Player.LifeState.State.VULNERABLE);
        
        if (lifeState.lives() == 3 && lifeState.state() == Player.LifeState.State.VULNERABLE) {
            assertTrue(true);
        }
        else {
            assertTrue(false);
        }
    }
    
    @Test
    public void lifeStateCanMoveTest() {
        LifeState lifeState0 = new LifeState(3, Player.LifeState.State.DEAD);
        LifeState lifeState1 = new LifeState(3, Player.LifeState.State.DYING);
        LifeState lifeState2 = new LifeState(3, Player.LifeState.State.INVULNERABLE);
        LifeState lifeState3 = new LifeState(3, Player.LifeState.State.VULNERABLE);
        
        if (!lifeState0.canMove() && !lifeState1.canMove() && lifeState2.canMove() && lifeState3.canMove()) {
            assertTrue(true);
        }
        else {
            assertTrue(false);
        }
    }
    
    @SuppressWarnings("unused")
    @Test (expected = NullPointerException.class)
    public void directedPositionConstructorFailPosition() {
        DirectedPosition directedPosition = new DirectedPosition(null, Direction.N);
    }
    
    @SuppressWarnings("unused")
    @Test (expected = NullPointerException.class)
    public void directedPositionConstructorFailDirection() {
        DirectedPosition directedPosition = new DirectedPosition(SubCell.centralSubCellOf(new Cell(0, 0)), null);
    }
    
    @SuppressWarnings("unused")
    @Test
    public void directedPositionConstructorAchieve() {
        DirectedPosition directedPosition = new DirectedPosition(SubCell.centralSubCellOf(new Cell(0, 0)), Direction.N);
    }
    
    @Test
    public void directedPositionAllGettersTest() {
        DirectedPosition directedPosition = new DirectedPosition(SubCell.centralSubCellOf(new Cell(0, 0)), Direction.N);
        
        if (SubCell.centralSubCellOf(new Cell(0, 0)).equals(directedPosition.position()) && Direction.N == directedPosition.direction()) {
            assertTrue(true);
        }
        else {
            assertTrue(false);
        }
    }
    
    @Test
    public void directedPositionWithDirectionOrPositionTest() {
        DirectedPosition directedPosition = new DirectedPosition(SubCell.centralSubCellOf(new Cell(0, 0)), Direction.N);
        directedPosition = directedPosition.withDirection(Direction.S);
        directedPosition = directedPosition.withPosition(SubCell.centralSubCellOf(new Cell(1, 1)));
        
        if (SubCell.centralSubCellOf(new Cell(1, 1)).equals(directedPosition.position()) && Direction.S == directedPosition.direction()) {
            assertTrue(true);
        }
        else {
            assertTrue(false);
        }
    }
    
    @Test
    public void directedPositionStoppedTest() {
        DirectedPosition temporaryDirectedPosition = new DirectedPosition(new SubCell(0,3), Direction.S);
        
        assertEquals(new SubCell(0, 3), DirectedPosition.stopped(temporaryDirectedPosition).tail().tail().head().position());
        assertEquals(Direction.S, DirectedPosition.stopped(temporaryDirectedPosition).tail().tail().head().direction());
    }
    
    @Test
    public void directedPositionMovingTest() {
        DirectedPosition temporaryDirectedPosition = new DirectedPosition(new SubCell(0,0), Direction.S);
        
        assertEquals(new SubCell(0, 3), DirectedPosition.moving(temporaryDirectedPosition).tail().tail().tail().head().position());
    }
    
    @Test
    public void statesForNextLifeTest() {
        Player test = new Player(PlayerID.PLAYER_1, 1, new Cell(0,0), 4, 4);
        
        assertEquals(State.DYING, test.statesForNextLife().head().state());
        assertEquals(1, test.statesForNextLife().head().lives());
    }
    
//    // Mettre completeSqOfStatesForNextLife en public pour le test
//    @Test
//    public void completeSqOfStatesForNextLifeTestDEAD() {
//          assertEquals(State.DEAD, Player.completeSqOfStatesForNextLife(0).head().state());
//          assertEquals(State.VULNERABLE, Player.completeSqOfStatesForNextLife(1).tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().tail().head().state());
//          assertEquals(State.INVULNERABLE, Player.completeSqOfStatesForNextLife(1).head().state());
//    }
}