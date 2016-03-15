package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;

/**
 * Immutable class. Handles representation of the game state (its players, bombs
 * explosions and explosion blasts).
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class GameState {
    private final int ticks;
    private final Board board;
    private final List<Player> players;
    private final List<Bomb> bombs;
    private final List<Sq<Sq<Cell>>> explosions;
    private final List<Sq<Cell>> blasts;

    /**
     * Main constructor.
     * 
     * @param ticks
     *            current tick
     * @param board
     *            board of the game
     * @param players
     *            a list of the four players
     * @param bombs
     *            the bombs on the board
     * @param explosions
     *            the current explosions
     * @param blasts
     *            the current explosion blasts
     * @throws IllegalArgumentException
     *             if ticks is strictly negative or players doesn't contain four
     *             players
     * @throws NullPointerException
     *             if the last five parameters are equal to null
     */
    public GameState(int ticks, Board board, List<Player> players,
            List<Bomb> bombs, List<Sq<Sq<Cell>>> explosions,
            List<Sq<Cell>> blasts)
                    throws IllegalArgumentException, NullPointerException {

        if (blasts == null || explosions == null || bombs == null
                || players == null || board == null) {
            throw new NullPointerException();
        }
        if (ticks < 0 || players.size() != 4) {
            throw new IllegalArgumentException();
        }

        this.ticks = ticks;
        this.board = board;
        this.players = players;
        this.bombs = bombs;
        this.explosions = explosions;
        this.blasts = blasts;
    }

    /**
     * Auxiliary constructor. Uses main constructor to build a game with given
     * board and players, no bombs, no explosions and no explosion blasts, and
     * sets ticks to 0. Useful to create the beginning of a game.
     * 
     * @param board
     *            board of the game
     * @param players
     *            list of all four players
     * @throws IllegalArgumentException
     *             if ticks is strictly negative or players doesn't contain four
     *             players
     * @throws NullPointerException
     *             if the last five parameters are equal to null
     */
    public GameState(Board board, List<Player> players)
            throws IllegalArgumentException, NullPointerException {
        this(0, board, players, new ArrayList<Bomb>(),
                new ArrayList<Sq<Sq<Cell>>>(), new ArrayList<Sq<Cell>>());
    }

    /**
     * Returns the current time of the game.
     * 
     * @return current ticks (representing the current time of the game)
     */
    public int ticks() {
        return ticks;
    }

    /**
     * Determines whether a game is over.
     * 
     * @return <b>true</b> if the time has run out (ticks equals TOTAL_TICKS) or
     *         if zero or one players are alive, <b>false</b> otherwise
     */
    public boolean isGameOver() {
        int nbAlivePlayers = 0;
        for (Player p : players) {
            if (p.isAlive())
                nbAlivePlayers++;
        }
        return (ticks == Ticks.TOTAL_TICKS || nbAlivePlayers <= 1);

    }

    /**
     * Returns the remaining time in the game (in seconds).
     * 
     * @return remaining time in seconds
     */
    public double remainingTime() {
        return ((double) (Ticks.TOTAL_TICKS - ticks))
                / ((double) Ticks.TICKS_PER_SECOND);
    }

    /**
     * Returns the winner of the game if there is one, or an optional empty
     * value if there is no winner.
     * 
     * @return winner of the game if there is one, or an optional empty value if
     *         there is no winner
     */
    public Optional<PlayerID> winner() {
        if (alivePlayers().size() == 1) {
            return Optional.of(alivePlayers().get(0).id());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Returns the board of the game.
     * 
     * @return board of the game
     */
    public Board board() {
        return board;
    }

    /**
     * Returns all the 4 players of the game (dead or alive).
     * 
     * @return list of all 4 players of the game (dead or alive)
     */
    public List<Player> players() {
        return players;
    }

    /**
     * Returns all alive players, i.e. all with players with at least one life.
     * 
     * @return all alive players
     */
    public List<Player> alivePlayers() {
        List<Player> alivePlayers = new ArrayList<Player>();
        for (Player p : players) {
            if (p.isAlive())
                alivePlayers.add(p);
        }

        return alivePlayers;
    }

    /**
     * public GameState next() {
     * 
     * }
     **/

    /**
     * Determines the state of all the blasts on time t+1, given the state of
     * the blasts, bombs and explosions on time t.
     * 
     * @param blasts0
     *            blasts on time t
     * @param board0
     *            board on time t
     * @param explosions0
     *            explosions on time t
     * @return blasts on time t+1
     */
    private static List<Sq<Cell>> nextBlasts(List<Sq<Cell>> blasts0,
            Board board0, List<Sq<Sq<Cell>>> explosions0) {
        List<Sq<Cell>> blasts1 = new ArrayList<Sq<Cell>>();

        for (Sq<Cell> blast : blasts0) {
            if (board0.blocksAt(blast.head()).head().isFree()
                    && !blast.tail().isEmpty()) {
                blasts1.add(blast.tail());
            }
        }

        for (Sq<Sq<Cell>> explosion : explosions0) {
            blasts1.add(explosion.head());
        }

        return blasts1;
    }

    public Map<Cell, Bomb> bombedCells() {
        Map<Cell, Bomb> bombCells = new HashMap<Cell, Bomb>();

        for (Bomb b : bombs) {
            bombCells.put(b.position(), b);
        }

        return bombCells;
    }

    public Set<Cell> blastedCells() {
        Set<Cell> blastCells = new HashSet<Cell>();

        for (Sq<Cell> b : blasts) {
            blastCells.add(b.head());
        }

        return blastCells;
    }

    public GameState next(Map<PlayerID, Optional<Direction>> speedChangeEvents,
            Set<PlayerID> bombDropEvents) {
        return null;
    }

    /*
     * private static List<Player> nextPlayers(List<Player> players0,
     * List<Sq<Cell>> blasts0) { List<Player> players1 = new
     * ArrayList<Player>();
     * 
     * for (Player p : players0) { for (Sq<Cell> b : blasts0) { if
     * (p.position().containingCell().equals(b.head())) { players1.add(new
     * Player(p.id(), p.statesForNextLife(), p.directedPositions().tail(),
     * p.maxBombs(), p.bombRange())); } }
     * 
     * players1.add(new Player(p.id(), p.lifeStates().tail(),
     * p.directedPositions().tail(), p.maxBombs(), p.bombRange()));
     * 
     * }
     * 
     * return players1; }
     * 
     * private static List<Sq<Sq<Cell>>> nextExplosions(List<Bomb> bombs0,
     * List<Sq<Sq<Cell>>> explosions0, List<Sq<Cell>> blasts0) {
     * List<Sq<Sq<Cell>>> explosions1 = new ArrayList<Sq<Sq<Cell>>>();
     * 
     * for (Sq<Sq<Cell>> e : explosions0) { if (!e.tail().isEmpty())
     * explosions1.add(e.tail()); }
     * 
     * for (Bomb bomb : bombs0) { if (bomb.fuseLength() == 1) { for
     * (Sq<Sq<Cell>> arm : bomb.explosion()) { explosions1.add(arm); } }
     * 
     * for (Sq<Cell> blast : blasts0) { if
     * (bomb.position().equals(blast.head())) { for (Sq<Sq<Cell>> arm :
     * bomb.explosion()) { explosions1.add(arm); } } } }
     * 
     * return explosions1; }
     */
}
