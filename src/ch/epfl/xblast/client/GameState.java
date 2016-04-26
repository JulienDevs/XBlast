package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

/**
 * Immutable Class. Handles a simple representation of the GameState for the
 * client
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
     * Main constructor. Construct the gameState given its players, board,
     * explosions, scores, time
     * 
     * @param players
     *            the given List of players
     * @param board
     *            the given board (List of images)
     * @param explosions
     *            the given explosions (List of images)
     * @param scores
     *            the given scores (List of images)
     * @param time
     *            the given time (List of images)
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
     * Getter of the players
     * 
     * @return players
     */

    public List<Player> players() {
        return players;
    }

    /**
     * Getter of the board
     * 
     * @return board
     */

    public List<Image> board() {
        return board;
    }

    /**
     * Getter of the explosions
     * 
     * @return explosions
     */

    public List<Image> explosions() {
        return explosions;
    }

    /**
     * Getter of the scores
     * 
     * @return scores
     */

    public List<Image> scores() {
        return scores;
    }

    /**
     * Getter of the time
     * 
     * @return time
     */

    public List<Image> time() {
        return time;
    }

    /**
     * Immutable Class. Handles a simple representation of a Player for the
     * client
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
         * Main Constructor. Construct the player from its id, lives, position
         * and image.
         * 
         * @param id
         *            given player id
         * @param lives
         *            given player's number of lives
         * @param position
         *            given player's positiokn
         * @param image
         *            given player's image
         */

        public Player(PlayerID id, int lives, SubCell position, Image image) {
            this.id = id;
            this.lives = lives;
            this.position = position;
            this.image = image;
        }

        /**
         * Getter of the player's id
         * 
         * @return id
         */

        public PlayerID id() {
            return id;
        }

        /**
         * Getter of the player's number of lives
         * 
         * @return lives
         */

        public int lives() {
            return lives;
        }

        /**
         * Getter of the player's position
         * 
         * @return position
         */

        public SubCell position() {
            return position;
        }

        /**
         * Getter of the player's image
         * 
         * @return image
         */
        public Image image() {
            return image;
        }
    }

}
