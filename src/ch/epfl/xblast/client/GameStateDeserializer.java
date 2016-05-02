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
 * Non Instantiable Class. Handles the deserialization of a GameState.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class GameStateDeserializer {
    private final static int NUMBER_INFORMATION_PER_PLAYER = 4;
    private final static byte FULL_TIME_BLOCK = 21;
    private final static byte EMPTY_TIME_BLOCK = 20;
    private static final byte IMAGE_CENTRAL_PART = 12;
    private static final byte START_SCORE_RECTANGLE = 10;
    private static final byte END_SCORE_RECTANGLE = 11;
    

    private static final ImageCollection explosionImages = new ImageCollection("explosion");
    private static final ImageCollection scoreImages = new ImageCollection("score");
    private static final ImageCollection playerImages = new ImageCollection("player");
    private static final ImageCollection boardImages = new ImageCollection("board");

    private GameStateDeserializer() {
    }

    /**
     * Return a Client GameState given a serialized state in bytes. Uses private
     * submethods
     * 
     * @param bytes
     *            the serialized state
     * @return the deserialized GameState
     */

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

        bytesForExplosions = RunLengthEncoder.decode(bytesForExplosions);

        for (byte b : bytesForExplosions) {
            explosions.add(explosionImages.imageOrNull(b));
        }

        return explosions;
    }
    
    private static List<Image> deserializeTime(byte remainingTime){
        List<Image> time = new ArrayList<>();
        
        time.addAll(Collections.nCopies(remainingTime, scoreImages.image(FULL_TIME_BLOCK)));
        time.addAll(Collections.nCopies(60 - remainingTime, scoreImages.image(EMPTY_TIME_BLOCK)));
        
        return time;
    }

    private static List<Player> deserializePlayers(List<Byte> bytesForPlayer) {
        List<GameState.Player> players = new ArrayList<>();
        for (int i = 0; i < bytesForPlayer.size(); i = i + 4) {
            PlayerID id = PlayerID.values()[i / 4];
            int lives = bytesForPlayer.get(i);
            SubCell position = new SubCell(
                    Byte.toUnsignedInt(bytesForPlayer.get(i + 1)),
                    Byte.toUnsignedInt(bytesForPlayer.get(i + 2)));
            Image img = playerImages.imageOrNull(bytesForPlayer.get(i + 3));
            players.add(new GameState.Player(id, lives, position, img));
        }
        return players;
    }

    private static List<Image> deserializeScore(
            List<GameState.Player> players) {
        List<Image> result = new ArrayList<>();
        for (int i = 0; i < players.size(); ++i) {
            if (i == 2) {
                result.addAll(Collections.nCopies(8,
                        scoreImages.image(IMAGE_CENTRAL_PART)));
            }

            int image = players.get(i).id().ordinal() * 2;
            if (players.get(i).lives() == 0) {
                image++;
            }
            result.add(scoreImages.image((byte) image));
            result.add(scoreImages.image(START_SCORE_RECTANGLE));
            result.add(scoreImages.image(END_SCORE_RECTANGLE));

        }
        return result;
    }

}
