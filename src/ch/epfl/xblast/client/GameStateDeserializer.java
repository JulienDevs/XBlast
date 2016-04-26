package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.client.GameState.Player;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class GameStateDeserializer {
    
    private static final byte IMAGE_CENTRAL_PART = 12;
    private static final byte START_SCORE_RECTANGLE = 10;
    private static final byte END_SCORE_RECTANGLE = 11;
    
    
    
    private GameStateDeserializer() {
    }

    public static GameState deserializeGameState(List<Byte> bytes) {
        
        

    }

    private static List<Image> deserializeBoard(List<Byte> bytesForBoard) {
        List<Image> board = new LinkedList<>();
        ImageCollection boardImages = new ImageCollection("board");

        bytesForBoard = RunLengthEncoder.decode(bytesForBoard);
        
        for (int i = 0; i < bytesForBoard.size(); ++i) {
            Cell c = Cell.SPIRAL_ORDER.get(i);
            int index = c.rowMajorIndex();
            
            board.add(index, boardImages.image(bytesForBoard.get(i)));
        }
        
        return board;
    }
    
    private static List<Image> deserializeExplosions(List<Byte> bytesForExplosions){
        List<Image> explosions = new ArrayList<>();
        return null;
        
    }
    

private static List<Player> deserializePlayers(List<Byte> bytesForPlayer){
    ImageCollection playerImages = new ImageCollection("player");
    List<GameState.Player> players = new ArrayList<>();
   for(int i =0; i<bytesForPlayer.size();i= i+4){
       PlayerID id = PlayerID.values()[i/4];
      int lives = bytesForPlayer.get(i);
      SubCell position = new SubCell(Byte.toUnsignedInt(bytesForPlayer.get(i+1)),Byte.toUnsignedInt(bytesForPlayer.get(i+2)));
      Image img = playerImages.imageOrNull(bytesForPlayer.get(i+3));
      players.add(new GameState.Player(id, lives, position, img));
   } 
    return players;
} 
    
    
private static List<Image> deserializeScore(List<GameState.Player> players){
    ImageCollection scoreImages = new ImageCollection("score");
    List<Image> result = new ArrayList<>();
    for(int i=0;i<players.size();++i){
       if(i==2){
           result.addAll(Collections.nCopies(8, scoreImages.image(IMAGE_CENTRAL_PART)));
       } 
       
       int image = players.get(i).id().ordinal()*2;
       if(players.get(i).lives()==0){
           image++;
       }
       result.add(scoreImages.image((byte)image));
       result.add(scoreImages.image(START_SCORE_RECTANGLE));
       result.add(scoreImages.image(END_SCORE_RECTANGLE));
        
    }
    return result;
    
}




}

