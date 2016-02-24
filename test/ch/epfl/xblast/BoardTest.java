package ch.epfl.xblast;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
**/


import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;

public class BoardTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void checkBlockMatrixThrowsExceptionOnNullMatrix() {
        List<List<Block>> lol = null;
        Board.checkBlockMatrix(lol, 2, 3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void checkBlockMatrixThrowsExceptionOnIncorrectMatrix() {
        List<List<Block>> lol = new ArrayList<List<Block>>();
        for(int i=0;i<10;i++){
        List<Block> lol2 = new ArrayList<Block>();   
        for(int j=0;j<15;j++){
           lol2.add(Block.FREE); 
        }
        lol.add(lol2);
           }
        List<Block> lol2 = new ArrayList<Block>();   
        for(int j=0;j<14;j++){
           lol2.add(Block.FREE); 
        }
        lol.add(lol2);
        
        Board.checkBlockMatrix(lol, 11, 15);
    }
    
    
    
    

}
