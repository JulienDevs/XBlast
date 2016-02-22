package xblast;

import ch.epfl.maze.util.Direction;

/**
 * Direction que le joueur peut prendre.
 * N pour nord
 * E pour est
 * S pour sud
 * W pour ouest
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */

//Rajouter "final" ou enum est implicitement final?
public enum Direction {
    N, E, S, W;
    
    /**
     * Reverses the direction.
     * 
     * @return The opposite direction.
     */

    public Direction opposite() {
        switch (this) {
        case N:
            return S;

        case S:
            return N;

        case E:
            return W;

        case W:
            return E;
            
        default:
            break;
        //direction default?
        }
    }
    
    /**
     * Returns true if and only if the direction is horizontal (i.e. E or W).
     * 
     * @return <b>true</b> if the direction is E or W,
     *         <b>false</b> otherwise
     */
    
    public boolean isHorizontal(){
        return this == E || this == W;
    }
    
    /**
     * Returns true if and only if the direction is parallel to the argument.
     * 
     * @param that - the direction to compare with.
     * @return <b>true</b> if the direction is parallel to the argument,
     *         <b>false</b> otherwise
     */
    
    public boolean isParallelTo(Direction that){
        return this == that || this == that.opposite();
    }
}
