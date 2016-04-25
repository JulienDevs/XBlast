package ch.epfl.xblast.server;

import ch.epfl.xblast.server.Player.LifeState.State;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class PlayerPainter {
    private final static byte STEP_FOR_PLAYER = 20;
    private final static byte INDEX_FOR_INVULNERABLE = 80;
    private final static byte STEP_FOR_DIRECTION = 3;
    private final static byte STEP_FOR_FIRST = 1;
    private final static byte STEP_FOR_SECOND = 2;
    private final static byte BYTE_FOR_DEAD = 100;

    private PlayerPainter() {
    }

    public static byte byteForPlayer(int tick, Player player) {
        if (!player.isAlive()) {
            return BYTE_FOR_DEAD;
        }
        byte bFP = 0;

        if (player.lifeState().state() == State.DYING) {
            bFP += (byte) ((player.lifeState().lives() > 1) ? 12 : 13);
        } else {
            bFP += player.direction().ordinal() * STEP_FOR_DIRECTION;

            int mod = (player.direction().isHorizontal())
                    ? player.position().x() % 4 : player.position().y() % 4;

            if (mod == 1) {
                bFP += STEP_FOR_FIRST;
            } else if (mod == 3) {
                bFP += STEP_FOR_SECOND;
            }
        }

        // Will never add INDEX_FOR_INVULNERABLE if the player is dying, so
        // won't try to reach for illegal indexes (like 92 or 93).
        bFP += (tick % 2 != 0
                && player.lifeState().state() == State.INVULNERABLE)
                        ? INDEX_FOR_INVULNERABLE
                        : player.id().ordinal() * STEP_FOR_PLAYER;

        return bFP;
    }
}
