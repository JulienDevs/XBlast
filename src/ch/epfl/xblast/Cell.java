
package ch.epfl.xblast;

/**
 * Immutable class.
 * Gives information about the board's dimension and
 * handles the order of the cells in the board (row major or spiral).
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
**/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.lang.Math;

public final class Cell {
    /**
     * Cells of the board in reading order.
     */
    public static final List<Cell> ROW_MAJOR_ORDER = Collections
            .unmodifiableList(rowMajorOrder());

    /**
     * Cells of the board in spiral order.
     */
    public static final List<Cell> SPIRAL_ORDER = Collections
            .unmodifiableList(rowSpiralOrder());

    /**
     * Number of columns in the board.
     */
    public final static int COLUMNS = 15;

    /**
     * Number of rows in the board.
     */
    public final static int ROWS = 13;

    /**
     * Number of cells in the board.
     */
    public final static int COUNT = COLUMNS * ROWS;

    private final int x;
    private final int y;

    /**
     * Constructor of Cell.
     * 
     * @param x
     *            - horizontal position
     * @param y
     *            - vertical position
     */
    public Cell(int x, int y) {
        this.x = Math.floorMod(x, COLUMNS);
        this.y = Math.floorMod(y, ROWS);
    }

    /**
     * Returns a list of the cells in the reading order
     * 
     * @return object of type ArrayList of Cells.
     */
    private static ArrayList<Cell> rowMajorOrder() {
        ArrayList<Cell> result = new ArrayList<Cell>();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                result.add(new Cell(j, i));

            }

        }
        return result;
    }

    /**
     * Returns a list of the cells in the spiral order
     * 
     * @return object of type ArrayList of Cells.
     */
    private static ArrayList<Cell> rowSpiralOrder() {
        ArrayList<Integer> ix = new ArrayList<Integer>();
        for (int i = 0; i < COLUMNS; i++) {
            ix.add(i);
        }
        ArrayList<Integer> iy = new ArrayList<Integer>();
        for (int i = 0; i < ROWS; i++) {
            iy.add(i);
        }

        boolean horizontal = true;
        ArrayList<Cell> result = new ArrayList<Cell>();
        while (ix.size() != 0 && iy.size() != 0) {
            ArrayList<Integer> i1 = new ArrayList<Integer>();
            ArrayList<Integer> i2 = new ArrayList<Integer>();

            if (horizontal) {
                i1 = ix;
                i2 = iy;
            } else {
                i1 = iy;
                i2 = ix;
            }

            int c2 = i2.get(0);
            i2.remove(0);
            for (int c1 : i1) {
                if (horizontal) {
                    result.add(ROW_MAJOR_ORDER.get(c2 * COLUMNS + c1));

                } else {
                    result.add(ROW_MAJOR_ORDER.get(c1 * COLUMNS + c2));
                }
            }
            horizontal = !horizontal;
            Collections.reverse(i1);
        }

        return result;
    }

    /**
     * Returns column of the cell.
     * 
     * @return column of the cell
     */
    public int x() {
        return x;
    }

    /**
     * Returns the row of the cell.
     * 
     * @return row of the cell
     */
    public int y() {
        return y;
    }

    /**
     * Returns the index of the cell in reading order.
     * 
     * @return the index of the cell in reading order
     */
    public int rowMajorIndex() {
        return y * COLUMNS + x;
    }

    /**
     * Returns the neighbor cell in a given direction, with respect to the
     * property that a border subcell's neighbor is on the opposite border.
     * 
     * @param dir
     *            - direction to the neighbor.
     * @return the neighbor cell in the direction d.
     */
    public Cell neighbor(Direction dir) {

        switch (dir) {
        case N:
            int y2 = Math.floorMod(y - 1, ROWS);

            return ROW_MAJOR_ORDER.get(y2 * COLUMNS + x);

        case S:
            int y3 = Math.floorMod(y + 1, ROWS);
            return ROW_MAJOR_ORDER.get(y3 * COLUMNS + x);

        case E:
            int x2 = Math.floorMod(x + 1, COLUMNS);
            return ROW_MAJOR_ORDER.get(y * COLUMNS + x2);

        case W:
            int x3 = Math.floorMod(x - 1, COLUMNS);
            return ROW_MAJOR_ORDER.get(y * COLUMNS + x3);

        default:
            return null;
        }
    }

    @Override
    public boolean equals(Object that) {
        if (that == null) {
            return false;
        } else {
            if (that.getClass() != getClass()) {
                return false;
            } else {
                return (this.x == ((Cell) that).x()
                        && this.y == ((Cell) that).y());
            }
        }
    }

    @Override
    public int hashCode() {
        return rowMajorIndex();
    }

}