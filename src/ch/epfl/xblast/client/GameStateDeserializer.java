package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.RunLengthEncoder;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class GameStateDeserializer {
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
        ImageCollection explosionImage
    }
}
