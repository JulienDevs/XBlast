package ch.epfl.xblast;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Class that defines the two generic methods mirrored and permutation, which
 * perform different operations on lists.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class Lists {

    private Lists() {

    }

    /**
     * Returns a symmetric version of the given list (without doubling the last
     * element).
     * 
     * @param List
     *            -list to be mirrored
     * @return list symmetric version of l
     * @throws IllegalArgumentException
     *             - if the list is null or empty
     */
    public static <T> List<T> mirrored(List<T> l) {
        if (l == null || l.isEmpty()) {
            throw new IllegalArgumentException();
        }
        List<T> list = new ArrayList<T>(l);
        List<T> tmpList = new ArrayList<T>(l.subList(0, l.size() - 1));
        Collections.reverse(tmpList);
        list.addAll(tmpList);
        list = Collections.unmodifiableList(list);
        return list;
    }

    /**
     * Returns all the permutations of the given list in an arbitrary order.
     * 
     * @param l
     *            - the list of which the permutations will be returned
     * @return A list containing all the permutations of l
     */
    public static <T> List<List<T>> permutations(List<T> l) {
        List<List<T>> list = new ArrayList<>(Arrays.asList());

        if (l == null || l.isEmpty()) {
            return new ArrayList<List<T>>();
        }

        if (l.size() == 2) {
            List<T> tmp = new ArrayList<>(l);

            list.add(new ArrayList<>(l));
            Collections.reverse(tmp);
            list.add(tmp);

            return list;
        }

        T firstElem = l.get(0);
        List<List<T>> subPermutations = permutations(l.subList(1, l.size()));

        for (int i = 0; i < subPermutations.size(); ++i) {
            for (int j = 0; j < l.size(); ++j) {
                List<T> tmp = new ArrayList<>(subPermutations.get(i));
                tmp.add(j, firstElem);
                list.add(tmp);
            }
        }

        return list;

    }
}
