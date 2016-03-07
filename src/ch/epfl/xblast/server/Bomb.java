package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;

/**
 * Immutable class.
 * Handles the state of the bombs on the board and their explosions.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
*/
public final class Bomb {
    public final PlayerID ownerId;
    public final Cell position;
    public final Sq<Integer> fuseLengths;
    public final int range;
    
    /**
     * Main constructor. Constructs a bomb given its owner,
     * position, fuse length and range.
     *
     * @param ownerId
     *          id of the player that dropped the bomb
     * @param position
     *          position where the bomb was dropped (a Cell)
     * @param fuseLengths
     *          number of ticks until the bomb will explode (current and future)
     * @param range
     *          range of the bomb's explosion
     * @throws NullPointerException
     *          if ownerId, position or fuseLenghts is null  
     * @throws IllegalArgumentException
     *          if fuseLengts is empty or range is strictly negative
     */
    public Bomb(PlayerID ownerId, Cell position, Sq<Integer> fuseLengths, int range) throws NullPointerException, IllegalArgumentException{
        if(ownerId == null || position == null || fuseLengths == null){
            throw new NullPointerException();
        } else if (fuseLengths.isEmpty()){
            throw new IllegalArgumentException();
        }
        this.ownerId = ownerId;
        this.position = position;
        this.fuseLengths = fuseLengths;
        this.range = ArgumentChecker.requireNonNegative(range);
    }
    
    /**
     * Auxiliary constructor that uses the main constructor. Passes a sequence
     * of decreasing numbers of size fuseLengths and all of its other parameters.
     * 
     * Parameters and throws same as the main constructor, except for fuseLengths.
     * @param ownerId
     * @param position
     * @param fuseLength
     *          number of ticks until the bomb will explode (the length
     *          of its wick)
     * @param range
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public Bomb(PlayerID ownerId, Cell position, int fuseLength, int range) throws NullPointerException, IllegalArgumentException{
        this(ownerId, position, Sq.iterate(fuseLength, u -> u - 1).limit(fuseLength), range);
    }
    
    /**
     * @return {@code PlayerId} that dropped the bomb    
     */ 
    public PlayerID ownerId(){ return ownerId; }
    
    /**
     * @return position where the bomb was dropped
     */
    public Cell position(){ return position; }
    
    /**
     * @return sequence of fuse length of the bomb (current and future)
     */
    public Sq<Integer> fuseLengths(){ return fuseLengths; }
    
    /**
     * @return current fuse length
     */
    public int fuseLength(){ return fuseLengths.head().intValue(); }
    
    /**
     * @return range of the explosion of the bomb
     */
    public int range(){ return range; }
    
    /**
     * Constructs the four arms of the explosion associated to the bomb. Its length is
     * given by the constant Ticks.TICKS_EXPLOSION. 
     * @return
     */
    public List<Sq<Sq<Cell>>> explosion(){
        List<Sq<Sq<Cell>>> arms = new ArrayList<Sq<Sq<Cell>>>();
        
        for(Direction dir : Direction.values()){
            arms.add(explosionArmTowards(dir));
        }
        
        return arms;
    }
    
    /**
     * Returns arm of the explosion going in the direction dir.
     * @param dir
     *          Direction in which the arm will be going
     * @return arm of the explosion as a sequence of sequence of cells,
     *          containing the state at each tick of each particle of the arm
     *          of the explosion.
     */
    //CHANGER VISIBILITE EN PRIVATE POUR LE RENDU!!!
    public Sq<Sq<Cell>> explosionArmTowards(Direction dir){
        Sq<Cell> arm = Sq.iterate(this.position, c -> c.neighbor(dir)).limit(range);
        return Sq.repeat(Ticks.EXPLOSION_TICKS, arm);
    }
}
