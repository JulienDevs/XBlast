package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;

/**
 * Immutable class. Handles the state of the bombs on the board and their
 * explosions. A bomb explodes if its fuseLengths sequence is over, or if a
 * blast hits it.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class Bomb {
    private final PlayerID ownerId;
    private final Cell position;
    private final Sq<Integer> fuseLengths;
    private final int range;

    /**
     * Main constructor. Constructs a bomb given its owner, position, fuse
     * length and range.
     *
     * @param ownerId
     *            - id of the player that dropped the bomb
     * @param position
     *            - position where the bomb was dropped (a Cell)
     * @param fuseLengths
     *            - number of ticks until the bomb will explode (current and
     *            future)
     * @param range
     *            - range of the bomb's explosion
     * @throws NullPointerException
     *             - if ownerId, position or fuseLenghts is null
     * @throws IllegalArgumentException
     *             - if fuseLengts is empty or range is strictly negative
     */
    public Bomb(PlayerID ownerId, Cell position, Sq<Integer> fuseLengths,
            int range) {
        Objects.requireNonNull(ownerId);
        Objects.requireNonNull(position);
        Objects.requireNonNull(fuseLengths);
        
        if (fuseLengths.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.ownerId = ownerId;
        this.position = position;
        this.fuseLengths = fuseLengths;
        this.range = ArgumentChecker.requireNonNegative(range);
    }

    /**
     * Auxiliary constructor that uses the main constructor. Passes a sequence
     * of decreasing numbers, whose size is fuseLengths, and all of its other
     * parameters.
     * 
     * @param ownerId
     *            - id of the player that dropped the bomb
     * @param position
     *            - position where the bomb was dropped (a Cell)
     * @param fuseLength
     *            - number of ticks until the bomb will explode (the length of
     *            its wick)
     * @param range
     *            - range of the bomb's explosion
     * @throws NullPointerException
     *             - if ownerId, position or fuseLenghts is null
     * @throws IllegalArgumentException
     *             - if fuseLengts is empty or range is strictly negative
     */
    public Bomb(PlayerID ownerId, Cell position, int fuseLength, int range) {
        this(ownerId, position,
                Sq.iterate(fuseLength, u -> u - 1).limit(fuseLength), range);
    }

    /**
     * Returns the id of the player that dropped the bomb.
     * 
     * @return id of the player that dropped the bomb
     */
    public PlayerID ownerId() {
        return ownerId;
    }

    /**
     * Returns the position
     * 
     * @return position where the bomb was dropped
     */
    public Cell position() {
        return position;
    }

    /**
     * Returns the sequence of fuse lengths of the bomb (current and future).
     * 
     * @return sequence of fuse lengths of the bomb (current and future)
     */
    public Sq<Integer> fuseLengths() {
        return fuseLengths;
    }

    /**
     * Returns the current fuse length of the bomb.
     * 
     * @return current fuse length
     */
    public int fuseLength() {
        return fuseLengths.head().intValue();
    }

    /**
     * Returns the range of the explosions of the bomb.
     * 
     * @return range of the explosion of the bomb
     */
    public int range() {
        return range;
    }

    /**
     * Constructs the four arms of the explosion associated to the bomb. Its
     * length is given by the constant Ticks.TICKS_EXPLOSION. The four arms are
     * represented as a list (the arms) of a sequence (the different blasts
     * emitted by the explosion) of a sequence (each blast and its futures
     * positions) of cells.
     * 
     * @return four arms of the explosion of the bomb
     */
    public List<Sq<Sq<Cell>>> explosion() {
        List<Sq<Sq<Cell>>> arms = new ArrayList<Sq<Sq<Cell>>>();

        for (Direction dir : Direction.values()) {
            arms.add(explosionArmTowards(dir));
        }

        return arms;
    }

    private Sq<Sq<Cell>> explosionArmTowards(Direction dir) {
        Sq<Cell> arm = Sq.iterate(this.position, c -> c.neighbor(dir))
                .limit(range);
        return Sq.repeat(Ticks.EXPLOSION_TICKS, arm);
    }
}
