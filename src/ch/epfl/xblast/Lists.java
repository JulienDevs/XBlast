package ch.epfl.xblast;

import java.util.List;
import java.util.Collections;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
*/
public final class Lists {
    
    /**
     * Private constructor of the non-instantiable Lists class.
     */
    private Lists(){
        
    }
    
    /**
     * Returns a symmetric version of a given list (without doubling
     * the last element).
     * 
     * @param List
     *          list to be mirrored
     * @return List - symmetric version of l
     * @throws IllegalArgumentException
     */
    public static <T> List<T> mirrored(List<T> l) throws IllegalArgumentException{
        if(l.isEmpty() || l == null){
            throw new IllegalArgumentException();
        }
        List<T> tmpList = l.subList(0, l.size() - 2);
        Collections.reverse(tmpList);
        l.addAll(tmpList);
        return l;
    }
}
