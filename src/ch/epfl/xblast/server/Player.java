package ch.epfl.xblast.server;

import java.util.Objects;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
**/
import ch.epfl.cs108.Sq;
import ch.epfl.xblast.ArgumentChecker;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

public final class Player {
    
    private final PlayerID id;
    private final Sq<LifeState> lifeStates;
    private final Sq<DirectedPosition> directedPos;
    private final int maxBombs;
    private final int bombRange;
    
    
    public Player(PlayerID id, Sq<LifeState> lifeStates, Sq<DirectedPosition> directedPos, int maxBombs, int bombRange)throws NullPointerException, IllegalArgumentException{
     this.id = id;
     this.lifeStates = Objects.requireNonNull(lifeStates);
     this.directedPos = Objects.requireNonNull(directedPos);
     this.maxBombs = Objects.requireNonNull(ArgumentChecker.requireNonNegative(maxBombs));
     this.bombRange = Objects.requireNonNull(ArgumentChecker.requireNonNegative(bombRange));
        
        
    }
    
   public Player(PlayerID id, int lives, Cell position, int maxBombs, int bombRange) throws NullPointerException, IllegalArgumentException{
       
       
       
   }
   
   
   
   public PlayerID id(){
     return id;  
   }
   
   public Sq<LifeState> lifeStates(){
       return lifeStates;
       
   }
   
   public LifeState lifeState(){
       return lifeStates.head();
   }
   
   public Sq<LifeState> statesForNextLife(){
       
       
   }
   
   public int lives(){
      
       
   }
   
  public boolean isAlive(){} 
   
  public Sq<DirectedPosition> directedPositions(){}
  
  public SubCell position(){}
  
  public Direction direction(){}
  
  
  public int maxBombs(){}
  
  public Player withMaxBombs(int newMaxBombs){}
  
  public int bombRange(){}
  
  public Player withBombRange(int newBombRange){}
  
  public Bomb newBomb(){}
  
  
  
  
  
  
  public final static class LifeState{
      
      private final int lives;
      private final State state;
     
   public LifeState(int lives, State state){
     this.lives = Objects.requireNonNull(ArgumentChecker.requireNonNegative(lives));
     this.state = Objects.requireNonNull(state);
       
   }   
   
   public int lives(){
       return lives;
       
   }
   
   public State state(){
       return state;
   }
   
   public boolean canMove(){
       
       return (state == State.INVULNERABLE || state == State.VULNERABLE);
       }
   
   public enum State {
       INVULNERABLE, VULNERABLE, DYING, DEAD;
   }
      
  }
  
 
  public final static class DirectedPosition{
      
  private final SubCell position;
  private final Direction direction;
  
  public DirectedPosition(SubCell position, Direction direction) throws NullPointerException{
   this.position = Objects.requireNonNull(position);
   this.direction = Objects.requireNonNull(direction);
  }    
      
  public static Sq<DirectedPosition> stopped(DirectedPosition p){
     return Sq.constant(p); 
      
  }
  
  
  public static Sq<DirectedPosition> moving(DirectedPosition p){
     return Sq.iterate(p, u->new DirectedPosition(u.position.neighbor(u.direction),u.direction)); 
      
  }
  
  
  public SubCell position(){
      return position;
  }
  
  public DirectedPosition withPosition(SubCell newPosition){
      return new DirectedPosition(newPosition,direction);
  }
  
  public Direction direction(){
      return direction;
  }
  
 public DirectedPosition withDirection(Direction newDirection){
     return new DirectedPosition(position,newDirection);
 }
  
  
  
      
  }
  
  
  
  
  
    

}
