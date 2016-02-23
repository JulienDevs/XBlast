package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Lists;

/**
 * 
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
*/
public final class Board {
    private final List<Sq<Block>> blocks;
    
    /**
     * Constructor of Board.
     * @param blocks
     *          The list of blocks that will compose the game board.
     * @throws IllegalArgumentException
     *          Throws IllegalArgumentException if the size of the
     *          block list is incorrect
     */
    public Board(List<Sq<Block>> blocks) throws IllegalArgumentException{
        if(blocks.size() != 195){
            throw new IllegalArgumentException();
        }
        this.blocks = blocks;
    }
    
    /**
     * Builds a constant board with a given matrix of blocks.
     * 
     * @param rows
     *          List containing the rows of blocks, which contains
     *          a list of the columns of blocks
     * @return board containing the blocks of rows
     * @throws IllegalArgumentException
     *          Throws IllegalArgumentException if the size of the
     *          block list is incorrect
     */
    
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
    
    /**
     * Builds a constant board with walls from a given matrix of the inner blocks.
     * 
     * @param innerBlocks
     *          List containing the rows of the inner blocks, which contains
     *          a list of the columns of the inner blocks
     * @return board containing the blocks of innerBlocks and the walls
     * @throws IllegalArgumentException
     *          Throws IllegalArgumentException if the size of the
     *          block list is incorrect
     */
    
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
    
    /**
     * Builds a constant board with walls from a given matrix of the north west
     * quadrant of blocks, which will be mirrored twice to form the whole board.
     * 
     * @param quandrantNWBBlocks
     *          List containing the rows of the north west quadrant, which contains
     *          a list of the columns of the north west quadrant
     * @return board containing the blocks of quadrantNWBlocks used to fill the
     *          whole board and the border walls
     * @throws IllegalArgumentException
     *          Throws IllegalArgumentException if the size of the
     *          block list is incorrect
     */
    
    public static Board ofQuadrantNWBlocksWalled(List<List<Block>> quadrantNWBBlocks) throws IllegalArgumentException{
        if(quadrantNWBBlocks.size() != 6){
            throw new IllegalArgumentException();
        }
        for(int k = 0 ; k < quadrantNWBBlocks.size() ; k++){
            if(quadrantNWBBlocks.get(k).size() != 7){
                throw new IllegalArgumentException();
            }
        }
        
        List<Sq<Block>> symmetricBlocks = new ArrayList<Sq<Block>>();
        
        for(int m = 0 ; m < quadrantNWBBlocks.size() ; m++){
            quadrantNWBBlocks.set(m, Lists.mirrored(quadrantNWBBlocks.get(m)));
        }
        quadrantNWBBlocks.addAll(Lists.mirrored(quadrantNWBBlocks));
        
        for(int i = 0 ; i < quadrantNWBBlocks.size() + 2 ; i++){
            for(int j = 0 ; j < quadrantNWBBlocks.get(i).size() + 2 ; j++){
                if(i == 0 || j == 0 || i == quadrantNWBBlocks.size() + 2 || j == quadrantNWBBlocks.get(i).size() + 2){
                    symmetricBlocks.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
                } else {
                    symmetricBlocks.add(Sq.constant(quadrantNWBBlocks.get(i).get(j)));
                }
            }
        }

        return new Board(symmetricBlocks);
    }
    
    
    /**
     * Returns the sequence of blocks of the cell c.
     * 
     * @param c
     *          Cell of which the sequence of blocks is to be returned
     * @return sequence of blocks of c
     */
    public Sq<Block> blocksAt(Cell c){
        return this.blocks.get(c.rowMajorIndex());
    }
    
    /**
     * Returns the head of the sequence of blocks of the cell c.
     * 
     * @param c
     *          Cell of which the head of the sequence of blocks is to be returned
     * @return head of the sequence of blocks of c
     */
    public Block blockAt (Cell c){
        return this.blocksAt(c).head();
    }
}
