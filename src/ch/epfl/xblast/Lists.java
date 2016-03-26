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
    private Lists(){
        
    }
    
    /**
     * Returns a symmetric version of a given list (without doubling
     * the last element).
     * 
     * @param List
     *          list to be mirrored
     * @return List
     *          symmetric version of l
     * @throws IllegalArgumentException
     */
    public static <T> List<T> mirrored(List<T> l) throws IllegalArgumentException{
        if(l == null || l.isEmpty()){
            throw new IllegalArgumentException();
        }
        List<T> list = new ArrayList<T>(l);
        List<T> tmpList = l.subList(0, l.size() - 1);
        Collections.reverse(tmpList);
        System.out.println(tmpList);
        System.out.println(l);
        list.addAll(tmpList);
        list = Collections.unmodifiableList(list);
        return list;
    }
    
    public static <T> List<List<T>> permutations(List<T> l){  
     List<T> subList = l.subList(l.size()-2, l.size());
     List<List<T>> lists = new ArrayList<List<T>>();
     lists.add(subList);
     List<T> subList2 = new ArrayList<T>();
     subList2.add(subList.get(1));
     subList2.add(subList.get(0));
     lists.add(subList2);
     System.out.println(lists);
     return recursion(lists,l);
    }
    
    private static <T> List<List<T>> recursion(List<List<T>> lists, List<T> l){
        int n = l.size();
        int fact = 1;
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        
      List<List<T>> newLists = new ArrayList<List<T>>();  
      for(List<T> tmpList : lists){
       for(int i =0;i<tmpList.size()+1;i++){
           List<T> temp = new ArrayList<T>(tmpList);
           temp.add(i, l.get(l.size()-tmpList.size()-1));
           newLists.add(temp);
       }
          
      }
      if(newLists.size() == fact){
          return newLists;
      }else{
          System.out.println(newLists);
          return recursion(newLists,l);
      }
    } 
}
