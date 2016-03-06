package ch.epfl.xblast.server;

import java.util.Objects;
import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.LifeState.State;
import java.util.function.*;

/**
 * Immutable class. Handles the representation of a Player
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */

public final class Player {

    private final PlayerID id;
    private final Sq<LifeState> lifeStates;
    private final Sq<DirectedPosition> directedPos;
    private final int maxBombs;
    private final int bombRange;

    /**
     * Main constructor. Constructs a Player given his id, LifeStates, sequence
     * of Directed Positions, the maximum of bombs he can drop and the range of
     * his bombs.
     *
     * @param id
     *            id of the player
     * @param lifeStates
     *            sequence of the lifeStates of the Player
     * @param directedPos
     *            sequence of the Directed Position of the Player
     * @param maxBombs
     *            the number of bomb that the player can drop at most
     * @param bombRange
     *            range of the bomb's explosion of the player
     * @throws NullPointerException
     *             if id, lifeStates or directedPos is null
     * @throws IllegalArgumentException
     *             if maxBombs or bombRange is strictly negative
     */

    public Player(PlayerID id, Sq<LifeState> lifeStates,
            Sq<DirectedPosition> directedPos, int maxBombs, int bombRange)
                    throws NullPointerException, IllegalArgumentException {
        this.id = Objects.requireNonNull(id);
        this.lifeStates = Objects.requireNonNull(lifeStates);
        this.directedPos = Objects.requireNonNull(directedPos);
        this.maxBombs = ArgumentChecker.requireNonNegative(maxBombs);
        this.bombRange = ArgumentChecker.requireNonNegative(bombRange);

    }

    /**
     * Auxiliary constructor that uses the main constructor. Construct a Player
     * given his id, number of lives,position, max bombs he can drop and their
     * range.
     * 
     * @param id
     *            id of the player
     * @param lives
     *            number of lives of the player
     * @param position
     *            Cell where the player is standing
     * @param maxBombs
     *            the number of bomb that the player can drop at most
     * @param bombRange
     *            range of the bomb's explosion of the player
     * @throws NullPointerException
     *             if id or position is null
     * @throws IllegalArgumentException
     *             if lives, maxBombs or bombRange is strictly negative
     */

    public Player(PlayerID id, int lives, Cell position, int maxBombs,
            int bombRange)
                    throws NullPointerException, IllegalArgumentException {
        this(id, createLifeStates(ArgumentChecker.requireNonNegative(lives)),
                DirectedPosition.stopped(new DirectedPosition(
                        SubCell.centralSubCellOf(
                                Objects.requireNonNull(position)),
                        Direction.S)),
                maxBombs, bombRange);
    }

    /**
     * @return player's id
     */
    public PlayerID id() {
        return id;
    }

    /**
     * @return player's sequence of LifeStates
     */
    public Sq<LifeState> lifeStates() {
        return lifeStates;

    }

    /**
     * @return player's LifeState
     */
    public LifeState lifeState() {
        return lifeStates.head();
    }

    /**
     * @return player's sequence of LifeStates for next life
     */
    public Sq<LifeState> statesForNextLife() {
        Sq<LifeState> init = Sq.repeat(Ticks.PLAYER_DYING_TICKS,
                new LifeState(lives(), State.DYING));
        return init.concat(createLifeStates(lives() - 1));
    }

    /**
     * @return player's number of lives
     */
    public int lives() {
        return lifeState().lives;

    }

    /**
     * @return true if the player is alive
     */
    public boolean isAlive() {
        return lives() > 0;
    }

    /**
     * @return the sequence of directed positions of the player
     */
    public Sq<DirectedPosition> directedPositions() {
        return directedPos;
    }

    /**
     * @return the position of the player
     */
    public SubCell position() {
        return directedPos.head().position;
    }

    /**
     * @return the direction of the player
     */
    public Direction direction() {
        return directedPos.head().direction;
    }

    /**
     * @return the number of bomb that the player can drop at most
     */
    public int maxBombs() {
        return maxBombs;
    }

    /**
     * @param newMaxBombs
     *            the number of bombs that the new player can drop
     * @return a new player identical with the current one with a different
     *         number of bombs the he can drop
     */
    public Player withMaxBombs(int newMaxBombs) {
        return new Player(id, lifeStates, directedPos, newMaxBombs, bombRange);
    }

    /**
     * @return the range of the player's bombs
     */
    public int bombRange() {
        return bombRange;
    }

    /**
     * @param newBombRange
     *            the range of the new player's bombs
     * @return a new player identical with the current one with a different bomb
     *         range
     */
    public Player withBombRange(int newBombRange) {
        return new Player(id, lifeStates, directedPos, maxBombs, newBombRange);
    }

    /**
     * @return a bomb placed on the cell the player is standing on, with a
     *         fuselenght of Ticks.BOMB_FUSE_TICKS and a range of the the
     *         bombrange of the player
     */
    public Bomb newBomb() {
        return new Bomb(id, position().containingCell(), Ticks.BOMB_FUSE_TICKS,
                bombRange);

    }

    /**
     * Create a sequence of LifeStates given a numbers of lives remaining
     * 
     * @param nbLives
     *            the number of lives remaining
     * @return the sequence of LifeStates
     */
    private static Sq<LifeState> createLifeStates(int nbLives) {
        if (nbLives == 0) {
            return Sq.constant(new LifeState(0, State.DEAD));
        } else {
            Sq<LifeState> init = Sq.repeat(Ticks.PLAYER_INVULNERABLE_TICKS,
                    new LifeState(nbLives, State.INVULNERABLE));
            Sq<LifeState> end = Sq
                    .constant(new LifeState(nbLives, State.VULNERABLE));
            return init.concat(end);
        }

    }

    /**
     * Immutable class. Handles the representation of a LifeState as two
     * informations : number of lives, and State
     * 
     * @author Yaron Dibner (257145)
     * @author Julien Malka (259041)
     */
    public final static class LifeState {

        private final int lives;
        private final State state;

        /**
         * Main constructor. Constructs a LifeState given a State and a number
         * of lives
         * 
         * @param lives
         *            number of lives of the LifeState
         * @param state
         *            State of the LifeState
         * 
         * @throws NullPointerException
         *             if state is null
         * @throws IllegalArgumentException
         *             if lives is strictly negative
         */
        public LifeState(int lives, State state)
                throws NullPointerException, IllegalArgumentException {
            this.lives = ArgumentChecker.requireNonNegative(lives);
            this.state = Objects.requireNonNull(state);

        }

        /**
         * @return the number of lives of the LifeState
         */
        public int lives() {
            return lives;

        }

        /**
         * @return the state of the LifeState
         */
        public State state() {
            return state;
        }

        /**
         * @return <b>true</b> if state of the player let's him move (i.e. invulnerable or vulnerable),
         *         <b>false</b> otherwise
         */
        
        public boolean canMove(){
            return (state == State.INVULNERABLE || state == State.VULNERABLE);
        }

        /**
         * All the States that a player can take
         * 
         * @author Yaron Dibner (257145)
         * @author Julien Malka (259041)
         */

        public enum State {
            INVULNERABLE, VULNERABLE, DYING, DEAD;
        }

    }

    /**
     * Immutable class. Handles the representation of a DirectedPosition as two
     * informations : a direction and a position
     * 
     * @author Yaron Dibner (257145)
     * @author Julien Malka (259041)
     */

    public final static class DirectedPosition {

        private final SubCell position;
        private final Direction direction;

        /**
         * Main constructor. Constructs a DirectedPosition given a position and
         * a direction
         * 
         * @param direction
         *            direction of the DirectedPosition
         * @param position
         *            position of the DirectedPosition
         * 
         * @throws NullPointerException
         *             if position or direction is null
         */

        public DirectedPosition(SubCell position, Direction direction)
                throws NullPointerException {
            this.position = Objects.requireNonNull(position);
            this.direction = Objects.requireNonNull(direction);
        }

        /**
         * Return a sequence of DirectedPosition representing a player stopped
         * at a given DirectedPosition
         * 
         * @param p
         *            the DirectedPosition where the player is stopped
         * 
         * @return an infinite sequence of Directed Positions
         */
        public static Sq<DirectedPosition> stopped(DirectedPosition p) {
            return Sq.constant(p);

        }

        /**
         * Return a sequence of DirectedPosition representing a player moving in
         * the direction he's looking at
         * 
         * @param p
         *            the DirectedPosition of the player
         * @return an infinite sequence of Directed Positions
         */
        public static Sq<DirectedPosition> moving(DirectedPosition p) {
            return Sq
                    .iterate(p,
                            u -> new DirectedPosition(
                                    u.position.neighbor(u.direction),
                                    u.direction));

        }

        /**
         * @return the position of the DirectedPostion
         */
        public SubCell position() {
            return position;
        }

        /**
         * Create a new DirectedPosition identical to the current one with a
         * different (given) position
         * 
         * @param newPosition
         *            the position of the new DirectedPosition
         * @return the new DirectedPosition
         */
        public DirectedPosition withPosition(SubCell newPosition) {
            return new DirectedPosition(newPosition, direction);
        }

        /**
         * @return the direction of the DirectedPosition
         */
        public Direction direction() {
            return direction;
        }

        /**
         * Create a new DirectedPosition identical to the current one with a
         * different (given) direction
         * 
         * @param newDirection
         *            the direction of the new DirectedPosition
         * @return the new DirectedPosition
         */
        public DirectedPosition withDirection(Direction newDirection) {
            return new DirectedPosition(position, newDirection);
        }

    }

}
