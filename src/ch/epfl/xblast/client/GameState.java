package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

/**
 * Immutable Class. Handles a simple representation of the GameState for the
 * client.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class GameState {
    private final List<Player> players;
    private final List<Image> board;
    private final List<Image> explosions;
    private final List<Image> scores;
    private final List<Image> time;

    /**
     * Main constructor. Constructs the GameState given its players, board,
     * explosions, scores and time.
     * 
     * @param players
     *            - the given List of players
     * @param board
     *            - the board of the game
     * @param explosions
     *            - the explosions of the game
     * @param scores
     *            - the scores (i.e. number of lives) of the players
     * @param time
     *            - the time since the game began
     */
    public GameState(List<Player> players, List<Image> board,
            List<Image> explosions, List<Image> scores, List<Image> time) {
        this.players = Collections.unmodifiableList(new ArrayList<>(players));
        this.board = Collections.unmodifiableList(new ArrayList<>(board));
        this.explosions = Collections
                .unmodifiableList(new ArrayList<>(explosions));
        this.scores = Collections.unmodifiableList(new ArrayList<>(scores));
        this.time = Collections.unmodifiableList(new ArrayList<>(time));
    }

    /**
     * Returns the fours players of the game.
     * 
     * @return the players of the game
     */
    public List<Player> players() {
        return players;
    }

    /**
     * Returns the images of the board of the game.
     * 
     * @return images of the board of the game
     */
    public List<Image> board() {
        return board;
    }

    /**
     * Returns the images of the bombs and the blasts of the game.
     * 
     * @return images of the bombs and the blasts of the game
     */

    public List<Image> explosions() {
        return explosions;
    }

    /**
     * Returns the images of the scores of the players.
     * 
     * @return images of the scores of the players
     */

    public List<Image> scores() {
        return scores;
    }

    /**
     * Returns the images of the time-line.
     * 
     * @return images of the time-line
     */

    public List<Image> time() {
        return time;
    }

    /**
     * Immutable Class. Handles a simple representation of a Player for the
     * client.
     * 
     * @author Yaron Dibner (257145)
     * @author Julien Malka (259041)
     */

    public final static class Player {
        private final PlayerID id;
        private final int lives;
        private final SubCell position;
        private final Image image;

        /**
         * Main Constructor. Constructs the player from its id, lives, position
         * and image.
         * 
         * @param id
         *            - id of the player
         * @param lives
         *            - player's number of lives
         * @param position
         *            - player's position
         * @param image
         *            - player's image
         */

        public Player(PlayerID id, int lives, SubCell position, Image image) {
            this.id = id;
            this.lives = lives;
            this.position = position;
            this.image = image;
        }

        /**
         * Returns the id of the player.
         * 
         * @return id of the player
         */
        public PlayerID id() {
            return id;
        }

        /**
         * Returns the player's number of lives.
         * 
         * @return player's number of lives
         */
        public int lives() {
            return lives;
        }

        /**
         * Returns the player's position.
         * 
         * @return player's position
         */
        public SubCell position() {
            return position;
        }

        /**
         * Returns the image representing the player.
         * 
         * @return image representing the player
         */
        public Image image() {
            return image;
        }
    }

}
