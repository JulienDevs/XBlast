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

    private final static byte ILLEGAL_BYTE = 100;

    private final static byte STEP_FOR_DYING = 12;
    private final static byte STEP_FOR_DEAD = 13;

    /**
     * Number of frames in the walking animation of the players.
     */
    private final static byte NB_WALKING_ANIMATION_STEPS = 4;

    /**
     * Position of the left foot frame in the walking animation of the players.
     */
    private final static byte LEFT_FOOT_IMAGE = 1;

    /**
     * Position of the right foot frame in the walking animation of the players.
     */
    private final static byte RIGHT_FOOT_IMAGE = 3;
    private final static byte STEP_FOR_LEFT_STEP = 1;
    private final static byte STEP_FOR_RIGHT_STEP = 2;

    private PlayerPainter() {
    }

    /**
     * Returns the byte corresponding to the image to use to represent the
     * player at a given tick.
     * 
     * @param tick
     *            - current tick
     * @param player
     *            - player for which the byte is asked
     * @return Byte corresponding to the image to use to represent the player.
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
                    ? player.position().x() % NB_WALKING_ANIMATION_STEPS
                    : player.position().y() % NB_WALKING_ANIMATION_STEPS;

            if (mod == LEFT_FOOT_IMAGE) {
                bFP += STEP_FOR_LEFT_STEP;
            } else if (mod == RIGHT_FOOT_IMAGE) {
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
