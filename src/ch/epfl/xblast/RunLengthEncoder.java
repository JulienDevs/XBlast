package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class containing two static methods that encode and decode a list of bytes
 * using the technique of run-length encoding.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class RunLengthEncoder {
    private static final int MAX_LENGTH = Byte.MAX_VALUE + 3;
    private static final int MIN_ENCODE_SIZE = 2;

    private RunLengthEncoder() {
    }

    /**
     * Encodes a given list of bytes using the technique of run-length encoding
     * and returns it.
     * 
     * @param bytes
     *            - the list to be encoded
     * @return the encoded list of bytes
     * @throws IllegalArgumentException
     *             - if one of the elements of bytes is negative
     */
    public static List<Byte> encode(List<Byte> bytes)
            throws IllegalArgumentException {
        for (byte b : bytes) {
            ArgumentChecker.requireNonNegative(b);
        }

        List<Byte> finalBytes = new ArrayList<>();

        List<Byte> sequence = new ArrayList<>();
        for (Byte b : bytes) {
            if (!(sequence.isEmpty() || sequence.contains(b))
                    || sequence.size() >= MAX_LENGTH) {
                if (sequence.size() <= MIN_ENCODE_SIZE) {
                    finalBytes.addAll(sequence);
                } else {
                    finalBytes.add((byte) -(sequence.size() - MIN_ENCODE_SIZE));
                    finalBytes.add(sequence.get(0));
                }
                sequence = new ArrayList<>();
            }
            sequence.add(b);
        }

        if (sequence.size() <= MIN_ENCODE_SIZE) {
            finalBytes.addAll(sequence);
        } else {
            finalBytes.add((byte) -(sequence.size() - MIN_ENCODE_SIZE));
            finalBytes.add(sequence.get(0));
        }

        return finalBytes;
    }

    /**
     * Decodes a given list of bytes using the technique of run-length encoding
     * backwards and returns it.
     * 
     * @param bytes
     *            - the list to be decoded
     * @return the decoded list of bytes
     * @throws IllegalArgumentException
     *             - if the last element of bytes is negative
     */
    public static List<Byte> decode(List<Byte> bytes)
            throws IllegalArgumentException {
        ArgumentChecker.requireNonNegative(bytes.get(bytes.size() - 1));

        List<Byte> finalBytes = new ArrayList<>();
        Iterator<Byte> it = bytes.listIterator();

        while (it.hasNext()) {
            Byte b = it.next();
            if (b < 0) {
                int n = Math.abs(b) + MIN_ENCODE_SIZE;
                Byte nextB = it.next();
                for (int i = 0; i < n; ++i) {
                    finalBytes.add(nextB);
                }
            } else {
                finalBytes.add(b);
            }
        }

        return finalBytes;
    }
}
