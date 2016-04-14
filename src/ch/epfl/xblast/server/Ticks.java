package ch.epfl.xblast.server;

import ch.epfl.xblast.Time;

/**
 * Interface containing all the constants defining the different durations (in
 * terms of ticks) in the game.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public interface Ticks {
    /**
     * Duration of the period during which a player is dying.
     */
    public final static int PLAYER_DYING_TICKS = 8;

    /**
     * Duration of the period during which a player is invulnerable.
     */
    public final static int PLAYER_INVULNERABLE_TICKS = 64;

    /**
     * Duration of life of a bomb (i.e. the time before it explodes).
     */
    public final static int BOMB_FUSE_TICKS = 100;

    /**
     * Duration of the explosion of a bomb.
     */
    public final static int EXPLOSION_TICKS = 30;

    /**
     * Duration of the collapse of a destructible wall.
     */
    public final static int WALL_CRUMBLING_TICKS = 30;

    /**
     * Duration of the disappearing of a bonus that was hit by a blast.
     */
    public final static int BONUS_DISAPPEARING_TICKS = 30;

    /**
     * Number of ticks in one second.
     */
    public final static int TICKS_PER_SECOND = 20;

    /**
     * Number of ticks in a nanosecond.
     */
    public final static int TICK_NANOSECOND_DURATION = Time.NS_PER_S
            / TICKS_PER_SECOND;

    /**
     * Total number of ticks in a game, which lasts 2 minutes.
     */
    public final static int TOTAL_TICKS = TICKS_PER_SECOND * Time.S_PER_MIN * 2;

}
