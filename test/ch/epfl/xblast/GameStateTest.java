package ch.epfl.xblast;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.Ticks;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public class GameStateTest {

    private Board testBoard = Board.boardForTestWithOnlyFreeBlocks();
    private List<Player> testPlayers = Player.fourPlayersForTest();
    private List<Bomb> testBombs = new ArrayList<Bomb>();
    private List<Sq<Sq<Cell>>> testExplosions = new ArrayList<Sq<Sq<Cell>>>();
    private List<Sq<Cell>> testBlasts = new ArrayList<Sq<Cell>>();

    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsExceptionOnIllegalArgumentTicks() {
        new GameState(-1, testBoard, testPlayers, testBombs, testExplosions,
                testBlasts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsExceptionOnIllegalArgumentPlayerSize() {
        new GameState(4, testBoard, Player.threePlayersForTest(), testBombs,
                testExplosions, testBlasts);
    }

    @Test(expected = NullPointerException.class)
    public void constructorThrowsExceptionOnNullBoard() {
        new GameState(4, null, testPlayers, testBombs, testExplosions,
                testBlasts);
    }

    @Test(expected = NullPointerException.class)
    public void constructorThrowsExceptionOnNullPlayers() {
        new GameState(4, testBoard, null, testBombs, testExplosions,
                testBlasts);
    }

    @Test(expected = NullPointerException.class)
    public void constructorThrowsExceptionOnNullBombs() {
        new GameState(4, testBoard, testPlayers, null, testExplosions,
                testBlasts);
    }

    @Test(expected = NullPointerException.class)
    public void constructorThrowsExceptionOnNullExplosions() {
        new GameState(4, testBoard, testPlayers, testBombs, null, testBlasts);
    }

    @Test(expected = NullPointerException.class)
    public void constructorThrowsExceptionOnNullBlasts() {
        new GameState(4, testBoard, testPlayers, testBombs, testExplosions,
                null);
    }

    @Test
    public void ticksGetterWorks() {
        GameState game = new GameState(50, testBoard, testPlayers, testBombs,
                testExplosions, testBlasts);
        int actual = game.ticks();
        int expected = 50;

        assertEquals(expected, actual);
    }

    @Test
    public void gameIsOverOnTOTALTICKS() {
        GameState gameOver = new GameState(Ticks.TOTAL_TICKS, testBoard,
                testPlayers, testBombs, testExplosions, testBlasts);

        assertTrue(gameOver.isGameOver());
    }

    @Test
    public void gameIsOverWithOnePlayer() {
        GameState gameOver = new GameState(4, testBoard,
                Player.oneAlivePlayerForTest(), testBombs, testExplosions,
                testBlasts);

        assertTrue(gameOver.isGameOver());
    }

    @Test
    public void gameIsOverWithZeroPlayers() {
        GameState gameOver = new GameState(4, testBoard,
                Player.fourDeadPlayersForTest(), testBombs, testExplosions,
                testBlasts);

        assertTrue(gameOver.isGameOver());
    }

    @Test
    public void remaingTimeOnBeginningIsTOTALTICKS() {
        GameState beginning = new GameState(testBoard, testPlayers);
        double expected = ((double) Ticks.TOTAL_TICKS)
                / ((double) (Ticks.TICKS_PER_SECOND));
        double actual = beginning.remainingTime();

        assertTrue(expected == actual);
    }

    @Test
    public void remaingTimeOnEndIs0() {
        GameState end = new GameState(Ticks.TOTAL_TICKS, testBoard, testPlayers,
                testBombs, testExplosions, testBlasts);
        double expected = 0.0;
        double actual = end.remainingTime();

        assertTrue(expected == actual);
    }

    @Test
    public void winnerReturnsEmptyOptionalObjectWhenFourPlayersAlive() {
        GameState game = new GameState(5, testBoard,
                Player.fourPlayersForTest(), testBombs, testExplosions,
                testBlasts);
        Optional<PlayerID> actual = game.winner();
        Optional<PlayerID> expected = Optional.empty();

        assertEquals(expected, actual);
    }

    @Test
    public void winnerReturnsTheWinnerWhenOneAlivePlayer() {
        GameState game = new GameState(5, testBoard,
                Player.oneAlivePlayerForTest(), testBombs, testExplosions,
                testBlasts);
        Optional<PlayerID> actual = game.winner();
        Optional<PlayerID> expected = Optional.of(PlayerID.PLAYER_1);

        assertEquals(expected, actual);
    }

    @Test
    public void winnerReturnsEmptyOptionalObjectWhenFourDeadPlayers() {
        GameState game = new GameState(5, testBoard,
                Player.fourDeadPlayersForTest(), testBombs, testExplosions,
                testBlasts);
        Optional<PlayerID> actual = game.winner();
        Optional<PlayerID> expected = Optional.empty();

        assertEquals(expected, actual);
    }

    @Test
    public void boardGetterWorks() {
        GameState game = new GameState(5, testBoard,
                Player.fourDeadPlayersForTest(), testBombs, testExplosions,
                testBlasts);

        assertEquals(game.board(), testBoard);
    }

    @Test
    public void playersGetterWorks() {
        GameState game = new GameState(5, testBoard, testPlayers, testBombs,
                testExplosions, testBlasts);

        assertEquals(testPlayers, game.players());
    }

    /**
     * Only players' id are tested, as there's no equals method in Player.
     */
    @Test
    public void alivePlayersWorksWithOneAlivePlayer() {
        GameState game = new GameState(5, testBoard,
                Player.oneAlivePlayerForTest(), testBombs, testExplosions,
                testBlasts);

        List<Player> actual = game.alivePlayers();
        List<Player> expected = new ArrayList<Player>();
        expected.add(Player.oneAlivePlayerForTest().get(0));

        boolean b = true;

        for (int i = 0; i < actual.size(); i++) {
            if()
        }
    }

    @Test
    public void alivePlayersWorksWithFourAlivePlayers() {
        GameState game = new GameState(5, testBoard, testPlayers, testBombs,
                testExplosions, testBlasts);

        List<Player> actual = game.alivePlayers();
        List<Player> expected = testPlayers;

        assertEquals(expected, actual);
    }
}
