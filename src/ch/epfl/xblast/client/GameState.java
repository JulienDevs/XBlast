package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class GameState {
    private final List<Player> players;
    private final List<Image> board;
    private final List<Image> explosions;
    private final List<Image> scores;
    private final List<Image> time;

    public GameState(List<Player> players, List<Image> board,
            List<Image> explosions, List<Image> scores, List<Image> time) {
        this.players = Collections.unmodifiableList(new ArrayList<>(players));
        this.board = Collections.unmodifiableList(new ArrayList<>(board));
        this.explosions = Collections
                .unmodifiableList(new ArrayList<>(explosions));
        this.scores = Collections.unmodifiableList(new ArrayList<>(scores));
        this.time = Collections.unmodifiableList(new ArrayList<>(time));
    }

    public List<Player> players() {
        return players;
    }

    public List<Image> board() {
        return board;
    }

    public List<Image> explosions() {
        return explosions;
    }

    public List<Image> scores() {
        return scores;
    }

    public List<Image> time() {
        return time;
    }

    public final static class Player {
        private final PlayerID id;
        private final int lives;
        private final SubCell position;
        private final Image image;
        
        public Player(PlayerID id, int lives, SubCell position, Image image) {
            this.id = id;
            this.lives = lives;
            this.position = position;
            this.image = image;
        }

        public PlayerID id() {
            return id;
        }

        public int lives() {
            return lives;
        }

        public SubCell position() {
            return position;
        }

        public Image image() {
            return image;
        }
    }

}
