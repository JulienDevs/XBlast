package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.Lists;
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

    private static final List<List<PlayerID>> PERMUTATION = Lists
            .permutations(Arrays.asList(PlayerID.PLAYER_1, PlayerID.PLAYER_2,
                    PlayerID.PLAYER_3, PlayerID.PLAYER_4));
    private static final Random RANDOM = new Random(2016);
    private static final Block[] PROBABILITY_BLOCKS = { Block.BONUS_BOMB,
            Block.BONUS_RANGE, Block.FREE };

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
     * Generates a new GameState on time t+1 given the state of the game on time
     * t.
     * 
     * @param speedChangeEvents
     *            changes in players directions on time t
     * @param bombDropEvents
     *            bombs dropped by players on time t
     * @return GameState on time t+1
     */
    public GameState next(Map<PlayerID, Optional<Direction>> speedChangeEvents,
            Set<PlayerID> bombDropEvents) {

        // 1. Evolution of blast

        List<Sq<Cell>> blasts1 = nextBlasts(blasts, board, explosions);

        // 2 Evolution of the board in function of the bonuses and the blasted
        // cells

        Set<Cell> consumedBonuses = new HashSet<Cell>();
        Map<PlayerID, Bonus> playerBonuses = new HashMap<PlayerID, Bonus>();
        Set<Cell> takenBonuses = new HashSet<Cell>(); // Bonuses already taken
                                                      // by players. Used to
                                                      // avoid that two players
                                                      // take the same bonus.

        // We sort the players in the order of the current permutation
        List<Player> players0 = new ArrayList<Player>();

        for (PlayerID id : PERMUTATION
                .get(Math.floorMod(RANDOM.nextInt(), PERMUTATION.size()))) {
            for (Player p : players) {
                if (p.id() == id) {
                    players0.add(p);
                }
            }
        }

        Cell c;

        for (Player p : players0) {
            c = p.position().containingCell();

            if (board.blockAt(c).isBonus()) {
                consumedBonuses.add(c);

                if (!takenBonuses.contains(c)) {
                    playerBonuses.put(p.id(),
                            board.blockAt(c).associatedBonus());
                    takenBonuses.add(c);
                }
            }
        }

        Set<Cell> blastedCells = new HashSet<Cell>();

        for (Sq<Cell> b : blasts1) {
            blastedCells.add(b.head());
        }

        Board board1 = nextBoard(board, consumedBonuses, blastedCells);

        // 3. Evolution of the explosions

        List<Sq<Sq<Cell>>> explosions1 = nextExplosions(explosions);

        // 4. Evolution of the bombs

        List<Bomb> bombs0 = newlyDroppedBombs(players0, bombDropEvents, bombs);
        List<Bomb> bombs1 = new ArrayList<Bomb>();
        Set<Cell> blastedCells1 = new HashSet<Cell>();

        for (Sq<Cell> b : blasts1) {
            blastedCells1.add(b.head());
        }

        bombs0.addAll(bombs);
        for (Bomb bomb : bombs0) {
            if (bomb.fuseLengths().tail().isEmpty()
                    || blastedCells1.contains(bomb.position())) {
                explosions1.addAll(bomb.explosion());

            } else {
                bombs1.add(new Bomb(bomb.ownerId(), bomb.position(),
                        bomb.fuseLengths().tail(), bomb.range()));

            }

        }

        // 5. Evolution of the Players

        Set<Cell> bombedCellsSet = new HashSet<Cell>();
        for (Bomb b : bombs1) {
            bombedCellsSet.add(b.position());
        }

        List<Player> players1 = nextPlayers(players, playerBonuses,
                bombedCellsSet, board1, blastedCells1, speedChangeEvents);

        return new GameState(ticks + 1, board1, players1, bombs1, explosions1,
                blasts1);

    }

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

    /**
     * 
     * @param players0
     * @param playerBonuses
     * @param bombedCells1
     * @param board1
     * @param blastedCells1
     * @param speedChangeEvents
     * @return
     */
    private static List<Player> nextPlayers(List<Player> players0,
            Map<PlayerID, Bonus> playerBonuses, Set<Cell> bombedCells1,
            Board board1, Set<Cell> blastedCells1,
            Map<PlayerID, Optional<Direction>> speedChangeEvents) {
        List<Player> players1 = new ArrayList<Player>();
        System.out.println(speedChangeEvents);

        // On parcours les joueurs
        for (Player player : players0) {
            System.out.println(player.id());
            System.out.println(player.direction());
            System.out.println(speedChangeEvents.get(player));

            Player.DirectedPosition actualDirection = player.directedPositions()
                    .head();
            Sq<Player.DirectedPosition> futurePositions;

            if (speedChangeEvents.containsKey(player)) {
                Optional<Direction> choice = speedChangeEvents.get(player);
                System.out.println(choice.isPresent());

                // Si le joueur ne veut pas tourner, ou qu'il ne peut pas
                // bouger, on l'arrete.
                if (choice == null || !player.lifeState().canMove()) {
                    futurePositions = Player.DirectedPosition
                            .stopped(actualDirection);

                } else { // Si le joueur veut tourner et qu'il peut bouger
                    // La prochaine sous-case central dans le chemin du joueur
                    Player.DirectedPosition cell = player.directedPositions()
                            .findFirst(p -> p.position().isCentral());

                    // Sequence de DirectedPosition jusqu'a la prochaine
                    // sous-case centrale (non-inclus) dans le chemin du joueur
                    Sq<Player.DirectedPosition> start = player
                            .directedPositions()
                            .takeWhile(p -> !p.position().isCentral());

                    // Sequence de DirectedPosition en partant de la prochaine
                    // sous-case central dans le chemin du joueure et allant
                    // dans la direction dans laquelle le joueur veut tourner
                    Sq<Player.DirectedPosition> end = Player.DirectedPosition
                            .moving(new Player.DirectedPosition(cell.position(),
                                    choice.get()));

                    futurePositions = start.concat(end);

                    // La position d'arret du joueur. C-a-d:
                    // - Cette position est à 6 sous-cases de la sous-case
                    // central de la case ou est posée une bombe
                    // - Cette position est à une sous-case central, dont la
                    // case voisine est un mur, et le joueur regarde ce mur
                    Player.DirectedPosition stopPosition = futurePositions
                            .findFirst(
                                    (Player.DirectedPosition p) -> ((bombedCells1
                                            .contains(p.position()
                                                    .containingCell())
                                            && p.position()
                                                    .distanceToCentral() == 6
                                            && p.position()
                                                    .neighbor(choice.get())
                                                    .distanceToCentral() == 5)
                                            || (!board1.blockAt(p.position()
                                                    .containingCell()
                                                    .neighbor(choice.get()))
                                                    .canHostPlayer()
                                                    && p.position()
                                                            .isCentral())));

                    start = futurePositions
                            .takeWhile(
                                    (Player.DirectedPosition p) -> !((bombedCells1
                                            .contains(p.position()
                                                    .containingCell())
                                    && p.position().distanceToCentral() == 6
                                    && p.position().neighbor(choice.get())
                                            .distanceToCentral() == 5)
                                    || (board1
                                            .blockAt(p.position()
                                                    .containingCell()
                                                    .neighbor(choice.get()))
                                            .castsShadow()
                                            && p.position().isCentral())));

                    end = Player.DirectedPosition.stopped(stopPosition);
                    futurePositions = start.concat(end);

                }

            } else { // J'ai pas encore décidé si ce block else était vraiment
                     // utile :))))
                futurePositions = Player.DirectedPosition
                        .moving(actualDirection);

                Player.DirectedPosition stopPosition = futurePositions
                        .findFirst(p -> (bombedCells1
                                .contains(p.position().containingCell())
                                && p.position().distanceToCentral() == 6
                                && p.position().neighbor(p.direction())
                                        .distanceToCentral() == 5
                                || (board1

                .blockAt(p.position().containingCell().neighbor(p.direction()))
                                        .castsShadow()
                                        && p.position().isCentral())));

                Sq<Player.DirectedPosition> start = futurePositions.takeWhile(
                        (Player.DirectedPosition p) -> !((bombedCells1
                                .contains(p.position().containingCell())
                                && p.position().distanceToCentral() == 6
                                && p.position().neighbor(p.direction())
                                        .distanceToCentral() == 5)
                                || (board1
                                        .blockAt(p.position().containingCell()
                                                .neighbor(p.direction()))
                                        .castsShadow()
                                        && p.position().isCentral())));

                Sq<Player.DirectedPosition> fin = Player.DirectedPosition
                        .stopped(stopPosition);
                futurePositions = start.concat(fin);
                futurePositions = futurePositions.tail();
            }

            Sq<Player.LifeState> futureLifeStates;
            if (blastedCells1.contains(futurePositions.head())) {
                futureLifeStates = player.statesForNextLife();
            } else {
                futureLifeStates = player.lifeStates();
            }

            if (playerBonuses.containsKey(player.id())) {
                player = playerBonuses.get(player.id()).applyTo(player);

            }

            players1.add(new Player(player.id(), futureLifeStates,
                    futurePositions, player.maxBombs(), player.bombRange()));

        }

        return players1;
    }

    /**
     * Determines the state of the board on time t+1, given the state of the
     * board, consumed bonuses and blasted cells on time t.
     * 
     * @param board0
     *            board on time t
     * @param consumedBonuses
     *            bonuses consumed by players on time t
     * @param blastedCells1
     *            cells containing a blast on time t
     * @return board on time t+1
     */
    private static Board nextBoard(Board board0, Set<Cell> consumedBonuses,
            Set<Cell> blastedCells1) {
        List<Sq<Block>> blocks1 = new ArrayList<Sq<Block>>();

        for (Cell c : Cell.ROW_MAJOR_ORDER) {
            if (consumedBonuses.contains(c)) {
                blocks1.add(Sq.constant(Block.FREE));
            } else if (blastedCells1.contains(c)) {
                if (board0.blockAt(c) == Block.DESTRUCTIBLE_WALL) {
                    blocks1.add((Sq.repeat(Ticks.WALL_CRUMBLING_TICKS,
                            Block.CRUMBLING_WALL)).concat(Sq.constant(
                                    PROBABILITY_BLOCKS[RANDOM.nextInt(3)])));
                } else if (board0.blockAt(c).isBonus()) {
                    Sq<Block> b = board0.blocksAt(c);
                    boolean alreadyBlasted = false;

                    for (int i = 0; i < Ticks.WALL_CRUMBLING_TICKS
                            && b.head() != Block.FREE; i++) {
                        b = b.tail();
                        if (b.head() == Block.FREE) {
                            alreadyBlasted = true;
                        }
                    }

                    if (!alreadyBlasted) {
                        blocks1.add(Sq.repeat(Ticks.BONUS_DISAPPEARING_TICKS,
                                board0.blockAt(c)));
                        blocks1.add(Sq.constant(Block.FREE));
                    } else {
                        blocks1.add(board0.blocksAt(c));
                    }
                } else {
                    blocks1.add(board0.blocksAt(c));
                }

            } else {
                blocks1.add(board0.blocksAt(c));
            }
        }

        return new Board(blocks1);
    }

    /**
     * Handles the aging of the explosions (and their aging only). The
     * explosions coming from exploding bombs is handled in next().
     * 
     * @param explosions0
     *            explosions on time t
     * @return explosions on time t+1, not including those created by exploding
     *         bombs.
     */
    private static List<Sq<Sq<Cell>>> nextExplosions(
            List<Sq<Sq<Cell>>> explosions0) {
        List<Sq<Sq<Cell>>> explosions1 = new ArrayList<Sq<Sq<Cell>>>();

        for (Sq<Sq<Cell>> e : explosions0) {
            if (!e.tail().isEmpty())
                explosions1.add(e.tail());
        }

        return explosions1;
    }

    /**
     * Returns a map containing all cells that have a bomb on it and the
     * associated bombs.
     * 
     * @return map of cells that have a bomb on it, and the associated bombs
     */
    public Map<Cell, Bomb> bombedCells() {
        Map<Cell, Bomb> bombCells = new HashMap<Cell, Bomb>();

        for (Bomb b : bombs) {
            bombCells.put(b.position(), b);
        }

        return bombCells;
    }

    /**
     * Returns a set with all the cells that have a blast on it.
     * 
     * @return set with all the cells that have a blast on it
     */
    public Set<Cell> blastedCells() {
        Set<Cell> blastCells = new HashSet<Cell>();

        for (Sq<Cell> b : blasts) {
            blastCells.add(b.head());
        }

        return blastCells;
    }

    private static List<Bomb> newlyDroppedBombs(List<Player> players0,
            Set<PlayerID> bombDropEvents, List<Bomb> bombs0) {
        List<Bomb> bombs1 = new ArrayList<Bomb>();
        List<Cell> bombedCells = new ArrayList<Cell>();

        for (Bomb b : bombs0) {
            bombedCells.add(b.position());
        }

        int nbBombs;

        for (Player player : players0) {
            nbBombs = 0;

            for (Bomb b : bombs0) {
                if (b.ownerId() == player.id()) {
                    nbBombs++;
                }
            }

            if (bombDropEvents.contains(player.id()) && player.isAlive()
                    && !bombedCells.contains(player.position().containingCell())
                    && player.maxBombs() > nbBombs) {
                bombs1.add(player.newBomb());
                bombedCells.add(player.position().containingCell());
            }
        }

        return bombs1;
    }

}
