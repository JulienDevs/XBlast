package ch.epfl.xblast.server;

/**
 * Non instantiable class. Represents an explosion painter. Can associate a bomb
 * or a blast with the image that represents it.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class ExplosionPainter {
    private static final byte BYTE_FOR_BLACK_BOMB = 20;
    private static final byte BYTE_FOR_WHITE_BOMB = 21;

    public static final byte BYTE_FOR_EMPTY = 16;

    private ExplosionPainter() {
    }

    /**
     * Returns byte corresponding to the image to use to represent a bomb
     * 
     * @param tick
     *            - the given bomb
     * @return Returns byte corresponding to the image to use to represent the
     *         bomb.
     */
    public static byte byteForBomb(Bomb bomb) {
        int f = bomb.fuseLength();
        boolean isPowerOf2 = ((f & (f - 1)) == 0);

        return isPowerOf2 ? BYTE_FOR_WHITE_BOMB : BYTE_FOR_BLACK_BOMB;
    }

    /**
     * Returns byte corresponding to the image to use to represent the blast.
     * 
     * @param blastInN
     *            - true iff there is a blast in the north neighbor cell
     * @param blastInE
     *            - true iff there is a blast in the east neighbor cell
     * @param blastInS
     *            - true iff there is a blast in the south neighbor cell
     * @param blastInW
     *            - true iff there is a blast in the west neighbor cell
     * @return Byte for the image to use to represent the blast.
     */
    public static byte byteForBlast(boolean blastInN, boolean blastInE,
            boolean blastInS, boolean blastInW) {
        int nFB = 0;
        nFB = (nFB << 1) | (blastInN ? 1 : 0);
        nFB = (nFB << 1) | (blastInE ? 1 : 0);
        nFB = (nFB << 1) | (blastInS ? 1 : 0);
        nFB = (nFB << 1) | (blastInW ? 1 : 0);

        return (byte) nFB;
    }
}
