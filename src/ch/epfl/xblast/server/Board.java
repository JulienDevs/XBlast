package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Lists;

/**
 * 
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
     *            The list of blocks that will compose the game board.
     * @throws IllegalArgumentException
     *             Throws IllegalArgumentException if the size of the block list
     *             is incorrect
     */
    public Board(List<Sq<Block>> blocks) throws IllegalArgumentException {
        if (blocks == null || blocks.size() != Cell.COUNT) {
            throw new IllegalArgumentException();
        }
        this.blocks = blocks;
    }

    /**
     * Builds a constant board with a given matrix of blocks.
     * 
     * @param rows
     *            List containing the rows of blocks, which contains a list of
     *            the columns of blocks
     * @return board containing the blocks of rows
     * @throws IllegalArgumentException
     *             Throws IllegalArgumentException if the size of the block list
     *             is incorrect
     */

    public static Board ofRows(List<List<Block>> rows)
            throws IllegalArgumentException {
        checkBlockMatrix(rows, 13, 15);
        List<Sq<Block>> constantBlocks = new ArrayList<Sq<Block>>();

        for (int i = 0; i < rows.size(); i++) {
            for (int j = 0; j < rows.get(i).size(); j++) {
                constantBlocks.add(Sq.constant(rows.get(i).get(j)));
            }
        }

        return new Board(constantBlocks);
    }

    /**
     * Builds a constant board with walls from a given matrix of the inner
     * blocks.
     * 
     * @param innerBlocks
     *            List containing the rows of the inner blocks, which contains a
     *            list of the columns of the inner blocks
     * @return board containing the blocks of innerBlocks and the walls
     * @throws IllegalArgumentException
     *             Throws IllegalArgumentException if the size of the block list
     *             is incorrect
     */

    public static Board ofInnerBlocksWalled(List<List<Block>> innerBlocks)
            throws IllegalArgumentException {
        checkBlockMatrix(innerBlocks, 11, 13);

        List<Sq<Block>> blocks = new ArrayList<Sq<Block>>();

        for (int i = 0; i < innerBlocks.size() + 2; i++) {
            for (int j = 0; j < innerBlocks.get(i).size() + 2; j++) {
                if (i == 0 || j == 0 || i == innerBlocks.size() + 2
                        || j == innerBlocks.get(i).size() + 2) {
                    blocks.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
                } else {
                    blocks.add(Sq.constant(innerBlocks.get(i).get(j)));
                }
            }
        }

        return new Board(blocks);
    }

    /**
     * Builds a constant board with walls from a given matrix of the north west
     * quadrant of blocks, which will be mirrored twice to form the whole board.
     * 
     * @param quandrantNWBBlocks
     *            List containing the rows of the north west quadrant, which
     *            contains a list of the columns of the north west quadrant
     * @return board containing the blocks of quadrantNWBlocks used to fill the
     *         whole board and the border walls
     * @throws IllegalArgumentException
     *             Throws IllegalArgumentException if the size of the block list
     *             is incorrect
     */

    public static Board ofQuadrantNWBlocksWalled(
            List<List<Block>> quadrantNWBBlocks)
                    throws IllegalArgumentException {
        checkBlockMatrix(quadrantNWBBlocks, 6, 7);

        List<Sq<Block>> symmetricBlocks = new ArrayList<Sq<Block>>();

        for (int m = 0; m < quadrantNWBBlocks.size(); m++) {
            quadrantNWBBlocks.set(m, Lists.mirrored(quadrantNWBBlocks.get(m)));
        }
        quadrantNWBBlocks = Lists.mirrored(quadrantNWBBlocks);

        for (int i = 0; i < quadrantNWBBlocks.size() + 2; i++) {
            for (int j = 0; j < quadrantNWBBlocks.get(2).size() + 2; j++) {
                if (i == 0 || j == 0 || i == quadrantNWBBlocks.size() + 1
                        || j == quadrantNWBBlocks.get(2).size() + 1) {
                    symmetricBlocks.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
                } else {
                    symmetricBlocks.add(Sq
                            .constant(quadrantNWBBlocks.get(i - 1).get(j - 1)));
                }
            }
        }

        return new Board(symmetricBlocks);
    }

    /**
     * Returns the sequence of blocks of the cell c.
     * 
     * @param c
     *            Cell of which the sequence of blocks is to be returned
     * @return sequence of blocks of c
     */
    public Sq<Block> blocksAt(Cell c) {

        return this.blocks.get(c.rowMajorIndex());
    }

    /**
     * Returns the head of the sequence of blocks of the cell c.
     * 
     * @param c
     *            Cell of which the head of the sequence of blocks is to be
     *            returned
     * @return head of the sequence of blocks of c
     */
    public Block blockAt(Cell c) {
        return this.blocksAt(c).head();
    }

    /**
     * Verify if the matrix given has the correct numbers of rows with each time
     * the correct numbers of columns
     * 
     * @param matrix
     *            Array of dimension 2
     * @param rows
     *            number of lines
     * @param columns
     *            number of columns
     * @throws IllegalArgumentException
     *             if the given matrix is not correct
     * 
     * 
     */

    private static void checkBlockMatrix(List<List<Block>> matrix, int rows,
            int columns) throws IllegalArgumentException {
        if (matrix == null) {
            throw new IllegalArgumentException();
        }
        if (matrix.size() != rows) {
            throw new IllegalArgumentException();

        }

        for (int k = 0; k < matrix.size(); k++) {
            if (matrix.get(k).size() != columns) {
                throw new IllegalArgumentException();

            }

        }

    }
}
