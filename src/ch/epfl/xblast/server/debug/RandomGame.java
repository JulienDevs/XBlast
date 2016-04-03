package ch.epfl.xblast.server.debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public class RandomGame {
    public static void main(String[] args) throws InterruptedException {
        List<List<Block>> board = new ArrayList<List<Block>>(Arrays.asList(
                Arrays.asList(Block.FREE, Block.FREE, Block.FREE, Block.FREE,
                        Block.FREE, Block.DESTRUCTIBLE_WALL, Block.FREE),
                Arrays.asList(Block.FREE, Block.INDESTRUCTIBLE_WALL,
                        Block.DESTRUCTIBLE_WALL, Block.INDESTRUCTIBLE_WALL,
                        Block.DESTRUCTIBLE_WALL, Block.INDESTRUCTIBLE_WALL,
                        Block.DESTRUCTIBLE_WALL),
                Arrays.asList(Block.FREE, Block.DESTRUCTIBLE_WALL, Block.FREE,
                        Block.FREE, Block.FREE, Block.DESTRUCTIBLE_WALL,
                        Block.FREE),
                Arrays.asList(Block.DESTRUCTIBLE_WALL,
                        Block.INDESTRUCTIBLE_WALL, Block.FREE,
                        Block.INDESTRUCTIBLE_WALL, Block.INDESTRUCTIBLE_WALL,
                        Block.INDESTRUCTIBLE_WALL, Block.INDESTRUCTIBLE_WALL),
                Arrays.asList(Block.FREE, Block.DESTRUCTIBLE_WALL, Block.FREE,
                        Block.DESTRUCTIBLE_WALL, Block.FREE, Block.FREE,
                        Block.FREE),
                Arrays.asList(Block.DESTRUCTIBLE_WALL,
                        Block.INDESTRUCTIBLE_WALL, Block.DESTRUCTIBLE_WALL,
                        Block.INDESTRUCTIBLE_WALL, Block.DESTRUCTIBLE_WALL,
                        Block.INDESTRUCTIBLE_WALL, Block.FREE)));
        List<Player> players = new ArrayList<Player>(Arrays
                .asList(new Player(PlayerID.PLAYER_1, 3, new Cell(1, 1), 2, 3),
                        new Player(PlayerID.PLAYER_2, 3, new Cell(13, 1), 2, 3),
                        new Player(PlayerID.PLAYER_3, 3, new Cell(13, 11), 2,
                                3),
                new Player(PlayerID.PLAYER_4, 3, new Cell(1, 11), 2, 3)));
        RandomEventGenerator randomEvent = new RandomEventGenerator(2016, 30,
                100);
        GameState game = new GameState(Board.ofQuadrantNWBlocksWalled(board),
                players);

        while (!game.isGameOver()) {
            Thread.sleep(1000);
            GameStatePrinter.printGameState(game);
            game = game.next(randomEvent.randomSpeedChangeEvents(),
                    randomEvent.randomBombDropEvents());
        }
    }
}
