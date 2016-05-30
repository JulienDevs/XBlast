package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.RunLengthEncoder;

/**
 * Class handling the serialization of a GameState. Uses the method
 * {@link ch.epfl.xblast.RunLengthEncoder#encode(List) encode} to compress the
 * information. The serialized GameState will be sent to the client.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class GameStateSerializer {

    private GameStateSerializer() {
    }

    /**
     * Returns the serialized version of the given GameState using the given
     * BoardPainter.
     * 
     * @param bPainter
     *            - the BoardPainter used to get the images associated with each
     *            block of the game
     * @param game
     *            - the state of the game
     * @return List of serialized bytes that represents the GameState.
     */
    public static List<Byte> serialize(BoardPainter bPainter, GameState game) {
        List<Byte> bytes = new ArrayList<>();

        // ---------- BOARD -----------

        List<Byte> bytesForBoard = new ArrayList<>();
        for (Cell c : Cell.SPIRAL_ORDER) {
            byte b = bPainter.byteForCell(game.board(), c);
            bytesForBoard.add(b);
        }

        // -----------EXPLOSIONS---------

        List<Byte> bytesForExplosions = new ArrayList<>();
        Set<Cell> blastedCells = game.blastedCells();
        Map<Cell, Bomb> bombedCells = game.bombedCells();
        for (Cell c : Cell.ROW_MAJOR_ORDER) {
            byte b = 0;

            if (blastedCells.contains(c)) {
                b = ExplosionPainter.byteForBlast(
                        blastedCells.contains(c.neighbor(Direction.N)),
                        blastedCells.contains(c.neighbor(Direction.E)),
                        blastedCells.contains(c.neighbor(Direction.S)),
                        blastedCells.contains(c.neighbor(Direction.W)));
            } else if (bombedCells.containsKey(c)) {
                b = ExplosionPainter.byteForBomb(bombedCells.get(c));
            } else {
                b = ExplosionPainter.BYTE_FOR_EMPTY;
            }

            bytesForExplosions.add(game.board().blockAt(c).canHostPlayer() ? b
                    : ExplosionPainter.BYTE_FOR_EMPTY);
        }

        bytesForBoard = RunLengthEncoder.encode(bytesForBoard);
        bytesForExplosions = RunLengthEncoder.encode(bytesForExplosions);

        bytes.add((byte) bytesForBoard.size());
        bytes.addAll(bytesForBoard);
        bytes.add((byte) bytesForExplosions.size());
        bytes.addAll(bytesForExplosions);

        // ----------PLAYERS------------
        
        for (Player p : game.players()) {
            bytes.add((byte) p.lives());
            bytes.add((byte) p.position().x());
            bytes.add((byte) p.position().y());
            bytes.add(PlayerPainter.byteForPlayer(game.ticks(), p));
        }

        // ----------TIME-----------

        bytes.add((byte) Math.ceil(game.remainingTime() / 2.0));

        return bytes;
    }
}
