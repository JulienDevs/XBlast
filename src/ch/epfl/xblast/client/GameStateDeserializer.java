package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.client.GameState.Player;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class GameStateDeserializer {
    private final static int NUMBER_INFORMATION_PER_PLAYER = 4;
    private final static byte FULL_TIME_BLOCK = 21;
    private final static byte EMPTY_TIME_BLOCK = 20;

    private GameStateDeserializer() {
    }

    public static GameState deserializeGameState(List<Byte> bytes) {
        int explosionIndex = bytes.get(0) + 1;
        int playerIndex = bytes.get(explosionIndex) + explosionIndex + 1;
        List<Byte> bytesForBoard = bytes.subList(1, bytes.get(0) + 1);
        List<Byte> bytesForExplosions = bytes.subList(explosionIndex + 1,
                bytes.get(explosionIndex) + explosionIndex + 1);
        List<Byte> bytesForPlayers = bytes.subList(playerIndex, playerIndex
                + PlayerID.values().length * NUMBER_INFORMATION_PER_PLAYER + 1);
        
        List<Player> players = deserializePlayers(bytesForPlayers);
        List<Image> board = deserializeBoard(bytesForBoard);
        List<Image> explosions = deserializeExplosions(bytesForExplosions);
        List<Image> score = deserializeScore(players);
        List<Image> time = deserializeTime(bytes.get(bytes.size() - 1));
        
        
        return new GameState(players, board, explosions, score, time);
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

    private static List<Image> deserializeExplosions(
            List<Byte> bytesForExplosions) {
        List<Image> explosions = new ArrayList<>();
        ImageCollection explosionImages = new ImageCollection("explosion");

        bytesForExplosions = RunLengthEncoder.decode(bytesForExplosions);

        for (byte b : bytesForExplosions) {
            explosions.add(explosionImages.imageOrNull(b));
        }

        return explosions;
    }
    
    private static List<Image> deserializeTime(byte remainingTime){
        List<Image> time = new ArrayList<>();
        ImageCollection scoreImages = new ImageCollection("score");
        
        time.addAll(Collections.nCopies(remainingTime, scoreImages.image((byte)21)));
        time.addAll(Collections.nCopies(60 - remainingTime, scoreImages.image((byte)20)));
        
        return time;
    }
}
