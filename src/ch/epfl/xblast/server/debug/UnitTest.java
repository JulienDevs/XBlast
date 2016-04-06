package ch.epfl.xblast.server.debug;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.Lists;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public class UnitTest {
    private final static Block __ = Block.FREE;
    private final static Block XX = Block.INDESTRUCTIBLE_WALL;
    private final static Block xx = Block.DESTRUCTIBLE_WALL;
    private static List<List<Block>> blockList;
    private static List<Player> players;
    private static GameState game;
    private static Map<PlayerID, Optional<Direction>> speedChangeEvents;
    private static Set<PlayerID> bombDropEvents;
    private static final List<List<PlayerID>> PERMUTATION = Lists
            .permutations(Arrays.asList(PlayerID.PLAYER_1, PlayerID.PLAYER_2,
                    PlayerID.PLAYER_3, PlayerID.PLAYER_4));

    private static int tickForBombDrop = 0;

    //@Test
    public void playersPutBombOnSameCell() {
        while (tickForBombDrop <= 100) {
            players = Arrays.asList(
                    new Player(PlayerID.PLAYER_1, 3, new Cell(13, 11), 3, 3),
                    new Player(PlayerID.PLAYER_2, 3, new Cell(13, 11), 3, 3),
                    new Player(PlayerID.PLAYER_3, 3, new Cell(13, 11), 3, 3),
                    new Player(PlayerID.PLAYER_4, 3, new Cell(13, 11), 3, 3));
            blockList = Arrays.asList(Arrays.asList(__, __, __, __, __, xx, __),
                    Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                    Arrays.asList(__, xx, __, __, __, xx, __),
                    Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                    Arrays.asList(__, xx, __, xx, __, __, __),
                    Arrays.asList(xx, XX, xx, XX, xx, XX, __));
            game = new GameState(Board.ofQuadrantNWBlocksWalled(blockList),
                    players);

            do {
                System.out.println("Tick: " + game.ticks());

                speedChangeEvents = speedChangeEvents(game.ticks());
                bombDropEvents = bombDropEvents(game.ticks());
                GameStatePrinter.printGameState(game);

                System.out.println(PERMUTATION
                        .get(Math.floorMod(game.ticks(), PERMUTATION.size())));

                game = game.next(speedChangeEvents, bombDropEvents);
            } while (game.ticks() <= tickForBombDrop);

            PlayerID playerThatPutBomb = game.bombedCells()
                    .get(players.get(0).position().containingCell()).ownerId();

            System.out.println(playerThatPutBomb);

            assertTrue(playerThatPutBomb == PERMUTATION
                    .get(Math.floorMod(tickForBombDrop, PERMUTATION.size()))
                    .get(0));
            tickForBombDrop++;
        }
    }

    /*
    @Test
    public void playersCatchBonusOnSameCell() {
        players = Arrays.asList(
                new Player(PlayerID.PLAYER_1, 3, new Cell(12, 11), 3, 3),
                new Player(PlayerID.PLAYER_2, 3, new Cell(12, 11), 3, 3),
                new Player(PlayerID.PLAYER_3, 3, new Cell(12, 11), 3, 3),
                new Player(PlayerID.PLAYER_4, 3, new Cell(12, 11), 3, 3));
        blockList = Arrays.asList(
                Arrays.asList(Block.BONUS_BOMB, __, __, __, __, xx, __),
                Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                Arrays.asList(__, xx, __, __, __, xx, __),
                Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                Arrays.asList(__, xx, __, xx, __, __, __),
                Arrays.asList(xx, XX, xx, XX, xx, XX, __));
        game = new GameState(Board.ofQuadrantNWBlocksWalled(blockList),
                players);

        do {
            System.out.println("Tick: " + game.ticks());

            speedChangeEvents = speedChangeEvents(game.ticks());
            bombDropEvents = bombDropEvents(-1);
            GameStatePrinter.printGameState(game);

            System.out.println(PERMUTATION
                    .get(Math.floorMod(game.ticks(), PERMUTATION.size())));

            game = game.next(speedChangeEvents, bombDropEvents);
        } while (game.players().get(0).position().containingCell().equals(new Cell(13,11)));

        assertTrue(playerThatPutBomb == PERMUTATION
                .get(Math.floorMod(tickForBombDrop, PERMUTATION.size()))
                .get(0));
    }
    */
    
    @Test
    public void playerGoesInWall(){
        players = Arrays.asList(
                new Player(PlayerID.PLAYER_1, 3, new Cell(13, 11), 3, 3),
                new Player(PlayerID.PLAYER_2, 3, new Cell(13, 11), 3, 3),
                new Player(PlayerID.PLAYER_3, 3, new Cell(13, 11), 3, 3),
                new Player(PlayerID.PLAYER_4, 3, new Cell(13, 11), 3, 3));
        blockList = Arrays.asList(Arrays.asList(__, __, __, __, __, xx, __),
                Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                Arrays.asList(__, xx, __, __, __, xx, __),
                Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                Arrays.asList(__, xx, __, xx, __, __, __),
                Arrays.asList(xx, XX, xx, XX, xx, XX, __));
        game = new GameState(Board.ofQuadrantNWBlocksWalled(blockList),
                players);
        
        do {
            speedChangeEvents = speedChangeEvents(game.ticks());
            bombDropEvents = bombDropEvents(-1);
            GameStatePrinter.printGameState(game);

            game = game.next(speedChangeEvents, bombDropEvents);
        } while (game.ticks() < 50);
    }

    private Map<PlayerID, Optional<Direction>> speedChangeEvents(int tick) {
        Map<PlayerID, Optional<Direction>> sce = new HashMap<>();

        if (tick == 0) {
            sce.put(PlayerID.PLAYER_1, Optional.of(Direction.W));
        }
        if(tick == 3){
            sce.put(PlayerID.PLAYER_1, Optional.of(Direction.N));
        }
        if(tick == 30){
            sce.put(PlayerID.PLAYER_1, Optional.of(Direction.E));
        }
        if(tick == 31){
            sce.put(PlayerID.PLAYER_1, Optional.of(Direction.N));
        }

        return sce;
    }

    private Set<PlayerID> bombDropEvents(int tick) {
        Set<PlayerID> bde = new HashSet<>();

        if (tick == tickForBombDrop) {
            bde.add(PlayerID.PLAYER_1);
            bde.add(PlayerID.PLAYER_2);
            bde.add(PlayerID.PLAYER_3);
            bde.add(PlayerID.PLAYER_4);
        }

        return bde;
    }
}
