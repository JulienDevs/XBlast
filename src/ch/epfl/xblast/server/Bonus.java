package ch.epfl.xblast.server;

/**
 * Enumaretion of the bonuses. These bonuses are applied to the players with the
 * function applyTo, which is redefined in the values of the enumeration.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 **/
public enum Bonus {
    INC_BOMB {
        @Override
        public Player applyTo(Player player) {
            if (player.maxBombs() < MAX_BONUSES) {
                return player.withMaxBombs(player.maxBombs() + 1);
            } else {
                return player;
            }
        }
    },

    INC_RANGE {
        @Override
        public Player applyTo(Player player) {
            if (player.bombRange() < MAX_BONUSES) {
                return player.withBombRange(player.bombRange() + 1);
            } else {
                return player;
            }
        }
    };

    private final static int MAX_BONUSES = 9;

    /**
     * Applies the bonus to the given player (i.e. increases its maxBombs or
     * range).
     * 
     * @param player
     *            - player which consumed the bonus
     * @return a new player with the applied bonus
     */
    abstract public Player applyTo(Player player);
}
