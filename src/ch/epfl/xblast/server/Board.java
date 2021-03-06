package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Lists;

/**
 * Immutable class. Represents a board of the game, defined as a list of blocks.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class Board {
    private final List<Sq<Block>> blocks;

    /**
     * Constructor of Board.
     * 
     * @param blocks
     *            - The list of blocks that will compose the game board.
     */
    public Board(List<Sq<Block>> blocks) {
        if (blocks == null || blocks.size() != Cell.COUNT) {
            throw new IllegalArgumentException();
        }
        this.blocks = Collections.unmodifiableList(new ArrayList<>(blocks));
    }

    /**
     * Builds a constant board with a given matrix of blocks.
     * 
     * @param rows
     *            - List containing the rows of blocks, which contains a list of
     *            the columns of blocks
     * @return board containing the blocks of rows
     */

    public static Board ofRows(List<List<Block>> rows) {
        checkBlockMatrix(rows, Cell.ROWS, Cell.COLUMNS);
        List<Sq<Block>> constantBlocks = new ArrayList<>();

        for (List<Block> l : rows) {
            constantBlocks.addAll(l.stream().map(b -> Sq.constant(b))
                    .collect(Collectors.toList()));
        }

        return new Board(constantBlocks);
    }

    /**
     * Builds a constant board with walls from a given matrix of the inner
     * blocks.
     * 
     * @param innerBlocks
     *            - List containing the rows of the inner blocks, which contains
     *            a list of the columns of the inner blocks
     * @return Board containing the blocks of innerBlocks and the walls
     */

    public static Board ofInnerBlocksWalled(List<List<Block>> innerBlocks) {
        checkBlockMatrix(innerBlocks, Cell.ROWS - 2, Cell.COLUMNS - 2);

        List<List<Block>> blocks = new ArrayList<>();

        blocks.add(
                Collections.nCopies(Cell.COLUMNS, Block.INDESTRUCTIBLE_WALL));

        List<Block> tmpBlocks;

        for (List<Block> l : innerBlocks) {
            tmpBlocks = new ArrayList<>();
            tmpBlocks.add(Block.INDESTRUCTIBLE_WALL);
            tmpBlocks.addAll(l);
            tmpBlocks.add(Block.INDESTRUCTIBLE_WALL);
            blocks.add(tmpBlocks);
        }

        blocks.add(
                Collections.nCopies(Cell.COLUMNS, Block.INDESTRUCTIBLE_WALL));

        return ofRows(blocks);
    }

    /**
     * Builds a constant board with walls from a given matrix of the north west
     * quadrant of blocks, which will be mirrored twice to form the whole board.
     * 
     * @param quandrantNWBBlocks
     *            - List containing the rows of the north west quadrant, which
     *            contains a list of the columns of the north west quadrant
     * @return board containing the blocks of quadrantNWBlocks used to fill the
     *         whole board and the border walls
     */

    public static Board ofQuadrantNWBlocksWalled(
            List<List<Block>> quadrantNWBBlocks) {
        checkBlockMatrix(quadrantNWBBlocks, (Cell.ROWS - 1) / 2,
                (Cell.COLUMNS - 1) / 2);

        List<List<Block>> symmetricBlocks = quadrantNWBBlocks.stream()
                .map(Lists::mirrored).collect(Collectors.toList());

        symmetricBlocks = Lists.mirrored(symmetricBlocks);

        return ofInnerBlocksWalled(symmetricBlocks);
    }

    /**
     * Returns the sequence of blocks of the cell c.
     * 
     * @param c
     *            -Cell of which the sequence of blocks is to be returned
     * @return sequence of blocks of c
     */
    public Sq<Block> blocksAt(Cell c) {
        return this.blocks.get(c.rowMajorIndex());
    }

    /**
     * Returns the head of the sequence of blocks of the cell c.
     * 
     * @param c
     *            - Cell of which the head of the sequence of blocks is to be
     *            returned
     * @return head of the sequence of blocks of c
     */
    public Block blockAt(Cell c) {
        return this.blocksAt(c).head();
    }

    /**
     * Verifes if the matrix given has the correct numbers of rows with each time
     * the correct numbers of columns.
     * 
     * @param matrix
     *            - Array of dimension 2
     * @param rows
     *            - number of lines
     * @param columns
     *            - number of columns
     */
    private static void checkBlockMatrix(List<List<Block>> matrix, int rows,
            int columns) throws IllegalArgumentException {
        if (matrix == null || matrix.size() != rows) {
            throw new IllegalArgumentException();
        }

        for (List<Block> l : matrix) {
            if (l.size() != columns) {
                throw new IllegalArgumentException();
            }
        }
    }
}
