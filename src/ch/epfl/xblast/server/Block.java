package ch.epfl.xblast.server;

/**
 * Different types of blocks of the board.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public enum Block {
    FREE, INDESTRUCTIBLE_WALL, DESTRUCTIBLE_WALL, CRUMBLING_WALL;

    /**
     * Returns true if and only if a block is free.
     * 
     * @return <b>true<\b> if and only if the block is free <b>false</b>
     *         otherwise
     */
    public boolean isFree() {
        return (this == FREE);
    }

    /**
     * Returns true if and only if the block can host a player.
     * 
     * @return <b>true<\b> if and only if the block can host a player
     *         <b>false</b> otherwise
     */
    public boolean canHostPlayer() {
        return this.isFree();
    }

    /**
     * Returns true if and only if the block casts a shadow on the board (i.e.
     * if it is a wall).
     * 
     * @return <b>true<\b> if and only if the block is a wall <b>false</b>
     *         otherwise
     */
    public boolean castsShadow() {
        return (this == INDESTRUCTIBLE_WALL || this == DESTRUCTIBLE_WALL
                || this == CRUMBLING_WALL);
    }
}
