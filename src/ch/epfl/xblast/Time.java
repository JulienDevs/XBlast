package ch.epfl.xblast;

/**
 * Constants for the time.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public interface Time {
    /**
     * Number of seconds in one minute.
     */
    public final static int S_PER_MIN = 60;

    /**
     * Number of milliseconds in one second.
     */
    public final static int MS_PER_S = 1000;

    /**
     * Number of microseconds in one second.
     */
    public final static int US_PER_S = 1000 * MS_PER_S;

    /**
     * Number of nanoseconds in one second.
     */
    public final static int NS_PER_S = 1000 * US_PER_S;
}
