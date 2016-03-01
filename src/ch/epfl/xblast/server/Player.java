package ch.epfl.xblast.server;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
**/
import ch.epfl.cs108.Sq;

public final class Player {
    
    public Player(PlayerID id, Sq<LifeState> lifeStates, Sq<DirectedPosition> directedPos, int maxBombs, int bombRange){
    }
    
   public Player(PlayerID id, int lives, Cell position, int maxBombs, int bombRange){
       
       
       
   }
   
   
   
   public PlayerID id(){
       
   }
   
   public LifeState lifeState(){
       
   }
   
   public Sq<LifeState> statesForNextLife(){
       
   }
   
   public int lives(){}
   
   
  public Sq<DirectedPosition> directedPositions(){}
  
  public SubCell position(){}
  
  public Direction direction(){}
  
  
  public int maxBombs(){}
  
  public Player withMaxBombs(int newMaxBombs){}
  
  public int bombRange(){}
  
  public Player withBombRange(int newBombRange){}
  
  public Bomb newBomb(){}
  
  
  
  
  
  
  public final static class LifeState{
      
   public LifeState(int lives, State state){
       
       
   }   
   
   public int lives(){}
   
   public State state(){}
   
   public boolean canMove(){}
   
   public enum State {
       INVULNERABLE, VULNERABLE, DYING, DEAD;
   }
      
  }
  
 
  public final static class DirectedPosition{
      
      
  public DirectedPosition(SubCell position, Direction direction){
      
      
  }    
      
  public static Sq<DirectedPosition> stopped(DirectedPosition p){}
  
  
  public static Sq<DirectedPosition> moving(DirectedPosition p){}
  
  
  public SubCell position(){
      
  }
  
  public Direction direction(){}
  
 public DirectedPosition withDirection(Direction newDirection){}
  
  
  
      
  }
  
  
  
  
  
    

}
