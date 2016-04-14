package ch.epfl.xblast;

/**
 * Class that contains a single method that checks if an integer is non
 * negative.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class ArgumentChecker {
    private ArgumentChecker() {
    }

    /**
     * Throws an IllegalArgumentException if a value is strictly negative.
     * Returns value otherwise.
     * 
     * @param value
     *            - integer to be checked
     * @return value is it is non negative.
     * @throws IllegalArgumentException
     *             if value is strictly negative.
     */
    public static int requireNonNegative(int value)
            throws IllegalArgumentException {
        if (value < 0) {
            throw new IllegalArgumentException();
        } else {
            return value;
        }
    }
}
