package ch.epfl.xblast;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class Lists {

    /**
     * Private constructor of the non-instantiable Lists class.
     */
    private Lists() {

    }

    /**
     * Returns a symmetric version of a given list (without doubling the last
     * element).
     * 
     * @param List
     *            list to be mirrored
     * @return List symmetric version of l
     * @throws IllegalArgumentException
     */
    public static <T> List<T> mirrored(List<T> l)
            throws IllegalArgumentException {
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
     * Returns all the permutations of the given list in an arbitrary order. The
     * computation of all the permutations is done in the private and static
     * method recursion(...).
     * 
     * @param l
     *            - the list of which the permutations will be returned
     * @return - a list containing all the permutations of l
     */
    public static <T> List<List<T>> permutations(List<T> l) {
        if(l.size()==0){
            List<List<T>> result =   new ArrayList<List<T>>();
            result.add(l);
            return result;
        }
        if(l.size()==1){
            List<List<T>> result =   new ArrayList<List<T>>();
            result.add(l);
            return result;
        }
        
        if(l.size()==2){
            List<List<T>> result =   new ArrayList<List<T>>();
            result.add(l);
            List<T> temp = new ArrayList<T>();
            temp.add(l.get(1));
            temp.add(l.get(0));
            result.add(temp);
            return result;  
            
        }
        
        
        
        List<T> subList = l.subList(l.size() - 2, l.size());
        List<List<T>> lists = new ArrayList<List<T>>();
        lists.add(subList);
        List<T> subList2 = new ArrayList<T>();
        subList2.add(subList.get(1));
        subList2.add(subList.get(0));
        lists.add(subList2);
        return recursion(lists, l);
    }

    
    private static <T> List<List<T>> recursion(List<List<T>> lists, List<T> l) {
        int n = l.size();
        int fact = 1;
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }

        List<List<T>> newLists = new ArrayList<List<T>>();
        for (List<T> tmpList : lists) {
            for (int i = 0; i < tmpList.size() + 1; i++) {
                List<T> temp = new ArrayList<T>(tmpList);
                temp.add(i, l.get(l.size() - tmpList.size() - 1));
                newLists.add(temp);
            }

        }
        if (newLists.size() == fact) {
            return newLists;
        } else {
            return recursion(newLists, l);
        }
    }
}
