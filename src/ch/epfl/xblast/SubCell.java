package ch.epfl.xblast;

import java.lang.Math;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
*/

public final class SubCell {
    public final static int COLUMNS = 240;
    public final static int ROWS = 208;
    
    private final int x, y;
    
    public SubCell(int x, int y){
        this.x = Math.floorMod(x, COLUMNS);
        this.y = Math.floorMod(y, ROWS);
    }
    
    /**
     * Returns the central subcell of a given Cell.
     * 
     * @param cell - Cell of which the central subcell is returned.
     * @return Object of type SubCell.
     */
    
    public static SubCell centralSubCellOf(Cell cell){
        int tmpX, tmpY;
        tmpX = cell.x() + 8;
        tmpY = cell.y() + 8;
        return new SubCell(tmpX, tmpY);
    }
    
    /**
     * Returns the Manhattan distance between the current subcell and the central subcell.
     * 
     * @return Intenger between 0 and 16 (maximum distance for a subcell and the center one).
     */
    
    public int distanceToCentral(){
        SubCell centralSubCell = centralSubCellOf(this.containingCell());
        int distance = Math.abs(this.x - centralSubCell.x()) + Math.abs(this.y - centralSubCell.y());
        return distance;
    }
    
    /**
     * Checks if the current subcell is the central subcell.
     * 
     * @return <b>true</b> if the subcell is the central one,
     *         <b>false</b> otherwise
     */
    
    public boolean isCentral(){
        return this.equals(centralSubCellOf(this.containingCell()));
    }
    
    /**
     * Returns the neighbor subcell in a given direction, with respect to the property that a border subcell's
     * neighbor is on the opposite border, like for the cells. 
     * 
     * @param d - direction to the neighbor.
     * @return The neighbor subcell in the direction d.
     */
    
    public SubCell neighbor(Direction d){
        int x = this.x;
        int y = this.y;
        
        switch(d) {
        case E:
            x = Math.floorMod(x++, COLUMNS);
            break;
        
        case W:
            x = Math.floorMod(x--, COLUMNS);
            break;
            
        case N:
            y = Math.floorMod(y--, ROWS);
            break;
            
        case S:
            y = Math.floorMod(y++, ROWS);
            break;
        
        default:
            return null;
        }
        
        return new SubCell(x, y);
    }
    
    /**
     * Returns the cell containing the current subcell.
     * 
     * @return Object of type {@code Cell}.
     */
    
    public Cell containingCell(){
        int cellX, cellY;
        
        cellX = this.x / 16;
        cellY = this.y / 16;
        
        return new Cell(cellX, cellY);
    }
    
    @Override
    public boolean equals(Object that){
        if(that == null){
            return false;
        } else {
            if(that.getClass() != getClass()){
                return false;
            } else {
                return (this.x == ((SubCell) that).x() && this.y == ((SubCell) that).y());
            }
        }
    }
    
    @Override
    public String toString(){
        return "(x " + " y)";
    }
    
    /**
     * @return x - column of the subcell.
     */
    public int x(){ return this.x; }
    
    /**
     * @return y - row of the subcell.
     */
    public int y(){ return this.y; }

}
