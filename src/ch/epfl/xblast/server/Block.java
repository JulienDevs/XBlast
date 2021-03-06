package ch.epfl.xblast.server;

import java.util.NoSuchElementException;

/**
 * Enumeration of the different blocks of the board.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public enum Block {
    FREE, INDESTRUCTIBLE_WALL, DESTRUCTIBLE_WALL, CRUMBLING_WALL, BONUS_BOMB(
            Bonus.INC_BOMB), BONUS_RANGE(Bonus.INC_RANGE);

    private final Bonus maybeAssociatedBonus;

    private Block(Bonus maybeAssociatedBonus) {
        this.maybeAssociatedBonus = maybeAssociatedBonus;
    }

    private Block() {
        this.maybeAssociatedBonus = null;
    }

    /**
     * Returns true if and only if a block is free.
     * 
     * @return <b>true</b> if and only if the block is free <b>false</b>
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
        return (this.isFree() || this.isBonus());
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

    /**
     * Return true if the block is a bonus.
     * 
     * @return true if the block is a bonus, false otherwise
     */
    public boolean isBonus() {
        return (this == BONUS_BOMB || this == BONUS_RANGE);
    }

    /**
     * Returns which type of bonus the block is.
     * 
     * @return which type of bonus the block is
     */
    public Bonus associatedBonus() {
        if (maybeAssociatedBonus == null) {
            throw new NoSuchElementException();
        } else {
            return maybeAssociatedBonus;
        }
    }
}
