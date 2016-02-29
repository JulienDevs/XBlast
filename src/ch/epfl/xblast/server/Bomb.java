package ch.epfl.xblast.server;

import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
*/
public final class Bomb {
    public final PlayerID ownerId;
    public final Cell position;
    public final Sq<Integer> fuseLengths;
    public final int range;
    
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
    
    public Bomb(PlayerID ownerId, Cell position, int fuseLength, int range){
        this(ownerId, position, Sq.iterate(fuseLength, u -> u - 1).limit(fuseLength), range);
    }
    
    public PlayerId ownerId(){ return ownerId; }
    public Cell position(){ return position; }
    public Sq<Integer> fuseLengths(){ return fuseLengths; }
    public int fuseLength(){ return fuseLengths.head().intValue(); }
    public int range(){ return range; }
    
    public List<Sq<Sq<Cell>>> explosion(){
        List<Sq<Sq<Cell>>> arms;
        
        arms.add(explosionArmTowards(Direction.N));
        arms.add(explosionArmTowards(Direction.E));
        arms.add(explosionArmTowards(Direction.S));
        arms.add(explosionArmTowards(Direction.W));
        
        return arms;
    }
    
    private Sq<Sq<Cell>> explosionArmTowards(Direction dir){
        Sq<Cell> arm = Sq.iterate(this.position, c -> c.neighbor(dir)).limit(range);
        return Sq.repeat(Ticks.EXPLOSION_TICKS, arm);
    }
}
