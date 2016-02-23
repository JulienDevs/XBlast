package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Lists;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
*/
public final class Board {
    private final List<Sq<Block>> blocks;
    
    public Board(List<Sq<Block>> blocks) throws IllegalArgumentException{
        if(blocks.size() != 195){
            throw new IllegalArgumentException();
        }
        this.blocks = blocks;
    }
    
    public static Board ofRows(List<List<Block>> rows) throws IllegalArgumentException{
        if(rows.size() != 13){
            throw new IllegalArgumentException();
        }
        for(int k = 0 ; k < rows.size() ; k++){
            if(rows.get(k).size() != 15){
                throw new IllegalArgumentException();
            }
        }
        
        List<Sq<Block>> constantBlocks = new ArrayList<Sq<Block>>();
        
        for(int i = 0 ; i < rows.size() ; i++){
            for(int j = 0 ; j < rows.get(i).size() ; j++){
                constantBlocks.add(Sq.constant(rows.get(i).get(j)));
            }
        }
        
        return new Board(constantBlocks);
    }
    
    public static Board ofInnerBlocksWalled(List<List<Block>> innerBlocks) throws IllegalArgumentException{
        if(innerBlocks.size() != 11){
            throw new IllegalArgumentException();
        }
        for(int k = 0 ; k < innerBlocks.size() ; k++){
            if(innerBlocks.get(k).size() != 13){
                throw new IllegalArgumentException();
            }
        }
        
        List<Sq<Block>> blocks = new ArrayList<Sq<Block>>();
        
        for(int i = 0 ; i < innerBlocks.size() + 2 ; i++){
            for(int j = 0 ; j < innerBlocks.get(i).size() + 2 ; j++){
                if(i == 0 || j == 0 || i == innerBlocks.size() + 2 || j == innerBlocks.get(i).size() + 2){
                    blocks.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
                } else {
                    blocks.add(Sq.constant(innerBlocks.get(i).get(j)));
                }
            }
        }
        
        return new Board(blocks);
    }
    
    public static Board ofQuadrantNWBlocksWalled(List<List<Block>> quandrantNWBBlocks) throws IllegalArgumentException{
        if(quandrantNWBBlocks.size() != 6){
            throw new IllegalArgumentException();
        }
        for(int k = 0 ; k < quandrantNWBBlocks.size() ; k++){
            if(quandrantNWBBlocks.get(k).size() != 7){
                throw new IllegalArgumentException();
            }
        }
        
        List<Sq<Block>> symmetricBlocks = new ArrayList<Sq<Block>>();
        
        for(int m = 0 ; m < quandrantNWBBlocks.size() ; m++){
            quandrantNWBBlocks.set(m, Lists.mirrored(quandrantNWBBlocks.get(m)));
        }
        quandrantNWBBlocks.addAll(Lists.mirrored(quandrantNWBBlocks));
        
        for(int i = 0 ; i < quandrantNWBBlocks.size() + 2 ; i++){
            for(int j = 0 ; j < quandrantNWBBlocks.get(i).size() + 2 ; j++){
                if(i == 0 || j == 0 || i == quandrantNWBBlocks.size() + 2 || j == quandrantNWBBlocks.get(i).size() + 2){
                    symmetricBlocks.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
                } else {
                    symmetricBlocks.add(Sq.constant(quandrantNWBBlocks.get(i).get(j)));
                }
            }
        }

        return new Board(symmetricBlocks);
    }
    
    public Sq<Block> blocksAt(Cell c){
        return this.blocks.get(c.rowMajorIndex());
    }
    
    public Block blockAt (Cell c){
        return this.blocksAt(c).head();
    }
    
    void checkBlockMatrix(List<List<Block>> matrix, int rows, int columns){
        if(matrix.size()!=rows || matrix.get(0).size()!=columns){
            throw new IllegalArgumentException();   
            
        }
        
    }
    
    
}
