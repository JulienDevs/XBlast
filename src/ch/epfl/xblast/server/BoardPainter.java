package ch.epfl.xblast.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;

/**
 * Immutable class representing the painter of the board.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class BoardPainter {
    private final Map<Block, BlockImage> palette;
    private final BlockImage shade;

    /**
     * Main constructor.
     * 
     * @param palette
     *            - map associating each block with its image
     * @param shade
     *            - the image for the shaded free blocks
     */
    public BoardPainter(Map<Block, BlockImage> palette, BlockImage shade) {
        this.palette = Collections.unmodifiableMap(new HashMap<>(palette));
        this.shade = shade;
    }

    /**
     * Returns byte corresponding to the image to use to represent a given cell.
     * 
     * @param board
     *            - the board of the game
     * @param c
     *            - the cell of which the byte is to be returned
     * @return The byte corresponding to the image to use to represent the cell.
     */
    public byte byteForCell(Board board, Cell c) {
        if (board.blockAt(c.neighbor(Direction.W)).castsShadow()
                && board.blockAt(c).isFree()) {
            return (byte) shade.ordinal();
        }
        return (byte) palette.get(board.blockAt(c)).ordinal();
    }
}
