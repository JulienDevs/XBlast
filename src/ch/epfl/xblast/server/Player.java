package ch.epfl.xblast.server;

import java.util.Objects;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
**/
import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Player.LifeState.State;

public final class Player {

    private final PlayerID id;
    private final Sq<LifeState> lifeStates;
    private final Sq<DirectedPosition> directedPos;
    private final int maxBombs;
    private final int bombRange;

    public Player(PlayerID id, Sq<LifeState> lifeStates,
            Sq<DirectedPosition> directedPos, int maxBombs, int bombRange)
                    throws NullPointerException, IllegalArgumentException {
        this.id = id;
        this.lifeStates = Objects.requireNonNull(lifeStates);
        this.directedPos = Objects.requireNonNull(directedPos);
        this.maxBombs = Objects
                .requireNonNull(ArgumentChecker.requireNonNegative(maxBombs));
        this.bombRange = Objects
                .requireNonNull(ArgumentChecker.requireNonNegative(bombRange));

    }

    public Player(PlayerID id, int lives, Cell position, int maxBombs,
            int bombRange)
                    throws NullPointerException, IllegalArgumentException {
        this(id, createLifeStates(ArgumentChecker.requireNonNegative(lives)),
                DirectedPosition.stopped(new DirectedPosition(
                        SubCell.centralSubCellOf(position), Direction.S)),
                maxBombs, bombRange);
    }

    public PlayerID id() {
        return id;
    }

    public Sq<LifeState> lifeStates() {
        return lifeStates;

    }

    public LifeState lifeState() {
        return lifeStates.head();
    }

    public Sq<LifeState> statesForNextLife() {
        Sq<LifeState> init = Sq.repeat(Ticks.PLAYER_DYING_TICKS,
                new LifeState(lives(), State.DYING));
        return init.concat(createLifeStates(lives()-1));
    }

    public int lives() {
        return lifeState().lives;

    }

    public boolean isAlive() {
        return lives()>0;
    }

    public Sq<DirectedPosition> directedPositions() {
        return directedPos;
    }

    public SubCell position() {
        return directedPos.head().position;
    }

    public Direction direction() {
        return directedPos.head().direction;
    }

    public int maxBombs() {
        return maxBombs;
    }

    public Player withMaxBombs(int newMaxBombs) {
        return new Player(id, lifeStates,directedPos, newMaxBombs,bombRange);
    }

    public int bombRange() {
        return bombRange;
    }

    public Player withBombRange(int newBombRange) {
        return new Player(id, lifeStates,directedPos, maxBombs,newBombRange);
    }

    public Bomb newBomb() {
        return new Bomb(id,position().containingCell(),Ticks.BOMB_FUSE_TICKS,bombRange);
        
    }

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

    public final static class LifeState {

        private final int lives;
        private final State state;

        public LifeState(int lives, State state) {
            this.lives = Objects
                    .requireNonNull(ArgumentChecker.requireNonNegative(lives));
            this.state = Objects.requireNonNull(state);

        }

        public int lives() {
            return lives;

        }

        public State state() {
            return state;
        }

        public boolean canMove() {

            return (state == State.INVULNERABLE || state == State.VULNERABLE);
        }

        public enum State {
            INVULNERABLE, VULNERABLE, DYING, DEAD;
        }

    }

    public final static class DirectedPosition {

        private final SubCell position;
        private final Direction direction;

        public DirectedPosition(SubCell position, Direction direction)
                throws NullPointerException {
            this.position = Objects.requireNonNull(position);
            this.direction = Objects.requireNonNull(direction);
        }

        public static Sq<DirectedPosition> stopped(DirectedPosition p) {
            return Sq.constant(p);

        }

        public static Sq<DirectedPosition> moving(DirectedPosition p) {
            return Sq
                    .iterate(p,
                            u -> new DirectedPosition(
                                    u.position.neighbor(u.direction),
                                    u.direction));

        }

        public SubCell position() {
            return position;
        }

        public DirectedPosition withPosition(SubCell newPosition) {
            return new DirectedPosition(newPosition, direction);
        }

        public Direction direction() {
            return direction;
        }

        public DirectedPosition withDirection(Direction newDirection) {
            return new DirectedPosition(position, newDirection);
        }

    }

}
