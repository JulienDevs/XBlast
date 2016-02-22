package ch.epfl.xblast;

/**
 /**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
**/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java.lang.Math;

public final class Cell {
    
   public final static int COLUMNS = 15;
   public final static int ROWS = 13;
   public final static int COUNT = COLUMNS * ROWS;
   private final int x;
   private final int y;
   public static final List<Cell> ROW_MAJOR_ORDER = Collections.unmodifiableList(rowMajorOrder());
   public static final List<Cell> ROW_SPIRAL_ORDER = Collections.unmodifiableList(rowSpiralOrder());
   
   public Cell(int x, int y){
     this.x = Math.floorMod(x,COLUMNS);
     this.y = Math.floorMod(y, ROWS);
       
       
   }
  
   private static ArrayList<Cell> rowMajorOrder() { 
       ArrayList<Cell> result = new ArrayList<Cell>();
       for(int i =0;i<ROWS;i++){
        for(int j=0;j<COLUMNS;j++){
          result.add(new Cell(j,i));  
            
        }   
           
       }
           
       
   }
   
   private static ArrayList<Cell> rowSpiralOrder() { 
      int ix1[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14};
      int iy1[] = {0,1,2,3,4,5,6,7,8,9,10,11,12};   
      ArrayList<Integer> ix = new ArrayList<Integer>();
      for(int i=0;i<COLUMNS-1;i++){
          ix.add(i);
      }
      ArrayList<Integer> iy = new ArrayList<Integer>();
      for(int i=0;i<ROWS-1;i++){
          iy.add(i);
      }
      
      
      boolean horizontal = true; 
      ArrayList<Cell> result = new ArrayList<Cell>();
      while(!(ix.size()==0 || iy.size()==0)){
          ArrayList<Integer> i1 = new ArrayList<Integer>();
          ArrayList<Integer> i2 = new ArrayList<Integer>();
         
          if(horizontal){
              i1 = ix;
              i2 = iy;
          }else{
              i1 = iy;
              i2 = ix;
          }
          
          int c2 = i2.get(0);
          i2.remove(0);
           
          
      }
       
           
       
   }
   
   
   
   
   
   
   
   
   
   public int x(){
       return x;
   }
   
   public int y(){
       return y;
   }
   
   public int rowMajorIndex(){
       
       
   }
   

}
=======
package xblast;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
**/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



import java.lang.Math;

public final class Cell {
    
   public final static int COLUMNS = 15;
   public final static int ROWS = 13;
   public final static int COUNT = COLUMNS * ROWS;
   private final int x;
   private final int y;
   public static final List<Cell> ROW_MAJOR_ORDER = Collections.unmodifiableList(rowMajorOrder());
   public static final List<Cell> SPIRAL_ORDER = Collections.unmodifiableList(rowSpiralOrder());
   
   public Cell(int x, int y){
     this.x = Math.floorMod(x,COLUMNS);
     this.y = Math.floorMod(y, ROWS);
       
       
   }
  
   private static ArrayList<Cell> rowMajorOrder() { 
       ArrayList<Cell> result = new ArrayList<Cell>();
       for(int i =0;i<ROWS;i++){
        for(int j=0;j<COLUMNS;j++){
          result.add(new Cell(j,i));  
            
        }   
           
       }
       return result;    
       
   }
   
   private static ArrayList<Cell> rowSpiralOrder() {    
      ArrayList<Integer> ix = new ArrayList<Integer>();
      for(int i=0;i<COLUMNS-1;i++){
          ix.add(i);
      }
      ArrayList<Integer> iy = new ArrayList<Integer>();
      for(int i=0;i<ROWS-1;i++){
          iy.add(i);
      }
      
      
      boolean horizontal = true; 
      ArrayList<Cell> result = new ArrayList<Cell>();
      while(!(ix.size()==0 || iy.size()==0)){
          ArrayList<Integer> i1 = new ArrayList<Integer>();
          ArrayList<Integer> i2 = new ArrayList<Integer>();
         
          if(horizontal){
              i1 = ix;
              i2 = iy;
          }else{
              i1 = iy;
              i2 = ix;
          }
          
          int c2 = i2.get(0);
          i2.remove(0);
          for(int c1 : i1){
              if(horizontal){
                  result.add(new Cell(c1,c2));
                  
              }else{
                  result.add(new Cell(c2,c1));
              }
          } 
          horizontal = !horizontal;
          Collections.reverse(i1);
      }
       
           
    return result;   
   }
   
   
   
   
   
   
   
   
   
   public int x(){
       return x;
   }
   
   public int y(){
       return y;
   }
   
   public int rowMajorIndex(){
for(int i =0;i<ROW_MAJOR_ORDER.size(); i++){
  if(ROW_MAJOR_ORDER.get(i).x==x && ROW_MAJOR_ORDER.get(i).y==y){
      
   return i;   
  }  
    
} 
return 0;
       
   }
   
   
 public Cell neighbor(Direction dir){
  
     switch (dir) {
     case N:
         int y2 = Math.floorMod(y-1, ROWS);
         return ROW_MAJOR_ORDER.get(y2*COLUMNS+x);

     case S:
          y2 = Math.floorMod(y+1, ROWS);
         return ROW_MAJOR_ORDER.get(y2*COLUMNS+x);


     case E:
         int x2 = Math.floorMod(x+1, COLUMNS);
         return ROW_MAJOR_ORDER.get(y*COLUMNS+x2);


     case W:
          x2 = Math.floorMod(x-1, ROWS);
         return ROW_MAJOR_ORDER.get(y*COLUMNS+x2);

         
     default:
         return null;
     }   
     
 }  
   
   

}

