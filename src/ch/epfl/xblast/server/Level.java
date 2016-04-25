package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Player.LifeState.State;

/**
 * Representation of a level of XBlast. Consists of a pair of a board painter
 * and the initial state of the game.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class Level {

    public final static Level DEFAULT_LEVEL = new Level(defaultBlockImage(),
            defaultGameState());

    private final static int DEFAULT_LIVES = 3;
    private final static int DEFAULT_MAX_BOMBS = 2;
    private final static int DEFAULT_RANGE = 3;

    private final BoardPainter boardPainter;
    private final GameState gameState;

    public Level(BoardPainter boardPainter, GameState gameState) {
        this.boardPainter = boardPainter;
        this.gameState = gameState;
    }

    public BoardPainter boardPainter() {
        return boardPainter;
    }

    public GameState gameState() {
        return gameState;
    }

    private static BoardPainter defaultBlockImage() {
        Map<Block, BlockImage> palette = new HashMap<Block, BlockImage>();

        palette.put(Block.FREE, BlockImage.IRON_FLOOR);
        palette.put(Block.INDESTRUCTIBLE_WALL, BlockImage.DARK_BLOCK);
        palette.put(Block.DESTRUCTIBLE_WALL, BlockImage.EXTRA);
        palette.put(Block.CRUMBLING_WALL, BlockImage.EXTRA_O);
        palette.put(Block.BONUS_BOMB, BlockImage.BONUS_BOMB);
        palette.put(Block.BONUS_RANGE, BlockImage.BONUS_RANGE);

        return new BoardPainter(palette, BlockImage.IRON_FLOOR_S);
    }

    private static GameState defaultGameState() {
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        Board board = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(Arrays.asList(__, __, __, __, __, xx, __),
                        Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                        Arrays.asList(__, xx, __, __, __, xx, __),
                        Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                        Arrays.asList(__, xx, __, xx, __, __, __),
                        Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
        
        Cell positions[] = { new Cell(1, 1),
                new Cell(13, 1), new Cell(13, 11), new Cell(1, 11) };
        
        List<Player> players = new ArrayList<>(Arrays.asList(
                new Player(PlayerID.PLAYER_1, DEFAULT_LIVES,
                        positions[PlayerID.PLAYER_1.ordinal()],
                        DEFAULT_MAX_BOMBS, DEFAULT_RANGE),
                new Player(PlayerID.PLAYER_2, DEFAULT_LIVES,
                        positions[PlayerID.PLAYER_2.ordinal()],
                        DEFAULT_MAX_BOMBS, DEFAULT_RANGE),
                new Player(PlayerID.PLAYER_3, DEFAULT_LIVES,
                        positions[PlayerID.PLAYER_3.ordinal()],
                        DEFAULT_MAX_BOMBS, DEFAULT_RANGE),
                new Player(PlayerID.PLAYER_4, DEFAULT_LIVES,
                        positions[PlayerID.PLAYER_4.ordinal()],
                        DEFAULT_MAX_BOMBS, DEFAULT_RANGE)));

        return new GameState(board, players);
    }
}
