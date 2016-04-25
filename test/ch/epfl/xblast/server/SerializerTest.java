package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.RunLengthEncoder;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public class SerializerTest {
    @Test
    public void serializerReturnsSameByteListAsTheExample() {
        Level level = Level.DEFAULT_LEVEL;

        List<Integer> expectedInt = new ArrayList<>(Arrays.asList(121, -50, 2, 1,
                -2, 0, 3, 1, 3, 1, -2, 0, 1, 1, 3, 1, 3, 1, 3, 1, 1, -2, 0, 1,
                3, 1, 3, -2, 0, -1, 1, 3, 1, 3, 1, 3, 1, 1, 2, 3, 2, 3, 2, 3, 2,
                3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3,
                2, 3, 2, 3, 2, 3, 2, 3, 1, 0, 0, 3, 1, 3, 1, 0, 0, 1, 1, 3, 1,
                1, 0, 0, 1, 3, 1, 3, 0, 0, -1, 1, 3, 1, 1, -5, 2, 3, 2, 3, -5,
                2, 3, 2, 3, 1, -2, 0, 3, -2, 0, 1, 3, 2, 1, 2, 4, -128, 16, -63,
                16, 3, 24, 24, 6, 3, -40, 24, 26, 3, -40, -72, 46, 3, 24, -72,
                66, 60));
        
        List<Byte> expectedByte = new ArrayList<>();
        for(int i : expectedInt){
            expectedByte.add((byte) i);
        }
        
        List<Byte> actual = GameStateSerializer.serialize(level.boardPainter(),
                level.gameState());
        
        assertEquals(expectedByte, actual);
    }
    
    @Test
    public void serializerOnExplosionsWorks(){
        GameState game = Level.DEFAULT_LEVEL.gameState();
        
        List<Byte> bytesForExplosions = new ArrayList<>();
        Set<Cell> blastedCells = game.blastedCells();
        Map<Cell, Bomb> bombedCells = game.bombedCells();
        for (Cell c : Cell.ROW_MAJOR_ORDER) {
            byte b = 0;

            if (blastedCells.contains(c)) {
                b = ExplosionPainter.byteForBlast(
                        blastedCells.contains(c.neighbor(Direction.N)),
                        blastedCells.contains(c.neighbor(Direction.E)),
                        blastedCells.contains(c.neighbor(Direction.S)),
                        blastedCells.contains(c.neighbor(Direction.W)));
            } else if (bombedCells.containsKey(c)) {
                b = ExplosionPainter.byteForBomb(bombedCells.get(c));
            } else {
                b = ExplosionPainter.BYTE_FOR_EMPTY;
            }

            bytesForExplosions.add(game.board().blockAt(c).isFree() ? b
                    : ExplosionPainter.BYTE_FOR_EMPTY);
        }
        
        System.out.println(bytesForExplosions.size());
        System.out.println(bytesForExplosions);
        
        System.out.println(RunLengthEncoder.encode(bytesForExplosions));
    }
}
