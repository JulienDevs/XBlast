package ch.epfl.xblast.server;

import ch.epfl.xblast.server.Player.LifeState.State;

/**
 * Non instantiable class. Represents a player painter. Can associate a player
 * with the image that represents it.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */

public final class PlayerPainter {
    private final static byte STEP_FOR_PLAYER = 20;
    private final static byte INDEX_FOR_INVULNERABLE = 80;
    private final static byte STEP_FOR_DIRECTION = 3;
    private final static byte STEP_FOR_LEFT_STEP = 1;
    private final static byte STEP_FOR_RIGHT_STEP = 2;
    private final static byte ILLEGAL_BYTE = 100;
    private final static byte STEP_FOR_DYING = 12;
    private final static byte STEP_FOR_DEAD = 13;

    private PlayerPainter() {
    }

    /**
     * Returns byte corresponding to the image to use to represent the player at
     * a given tick
     * 
     * @param tick
     *            - the given tick
     * @param player
     *            - the given player
     * @return the byte corresponding to the image to use to represent the
     *         player.
     */
    public static byte byteForPlayer(int tick, Player player) {
        if (!player.isAlive()) {
            return ILLEGAL_BYTE;
        }
        byte bFP = 0;

        if (player.lifeState().state() == State.DYING) {
            bFP += (byte) ((player.lifeState().lives() > 1) ? STEP_FOR_DYING
                    : STEP_FOR_DEAD);
        } else {
            bFP += player.direction().ordinal() * STEP_FOR_DIRECTION;

            int mod = (player.direction().isHorizontal())
                    ? player.position().x() % 4 : player.position().y() % 4;

            if (mod == 1) {
                bFP += STEP_FOR_LEFT_STEP;
            } else if (mod == 3) {
                bFP += STEP_FOR_RIGHT_STEP;
            }
        }

        bFP += (tick % 2 != 0
                && player.lifeState().state() == State.INVULNERABLE)
                        ? INDEX_FOR_INVULNERABLE
                        : player.id().ordinal() * STEP_FOR_PLAYER;

        return bFP;
    }
}
