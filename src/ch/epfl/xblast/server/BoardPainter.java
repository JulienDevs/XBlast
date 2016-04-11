package ch.epfl.xblast.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class BoardPainter {
    private final Map<Block, BlockImage> palette;
    private final BlockImage shade;

    public BoardPainter(Map<Block, BlockImage> palette, BlockImage shade) {
        this.palette = Collections.unmodifiableMap(new HashMap(palette));
        this.shade = shade;
    }

    public byte byteForCell(Board board, Cell c) {
        if (board.blockAt(c.neighbor(Direction.W)).castsShadow()
                && board.blockAt(c).isFree()) {
            return (byte) shade.ordinal();
        }
        return (byte) palette.get(board.blockAt(c)).ordinal();
    }
}
