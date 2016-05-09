package ch.epfl.xblast.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public final class Main {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        GameState game = Level.DEFAULT_LEVEL.gameState();
        BoardPainter bPainter = Level.DEFAULT_LEVEL.boardPainter();
        int expectedClients = 4;
        ByteBuffer buffer;
        Map<SocketAddress, PlayerID> ips = new HashMap<>();

        if (!(args == null || args[0] == null || args[0].length() == 0))
            expectedClients = Integer.parseInt(args[0]);

        DatagramChannel channel = DatagramChannel
                .open(StandardProtocolFamily.INET);
        channel.bind(new InetSocketAddress(2016));

        buffer = ByteBuffer.allocate(1);
        SocketAddress senderAddress;
        while (ips.size() < expectedClients) {
            senderAddress = channel.receive(buffer);
            if (buffer.get(0) == PlayerAction.JOIN_GAME.ordinal()) {
                ips.put(senderAddress, PlayerID.values()[ips.size()]);

            }
        }

        while (game.isGameOver()) {
            List<Byte> serializedGameState = GameStateSerializer
                    .serialize(bPainter, game);

            for (Map.Entry<SocketAddress, PlayerID> e : ips.entrySet()) {
                buffer = ByteBuffer.allocate(serializedGameState.size() + 1);

                buffer.put((byte) e.getValue().ordinal());
                for (byte b : serializedGameState) {
                    buffer.put(b);
                }

                buffer.flip();
                channel.send(buffer, e.getKey());
                
                channel.configureBlocking(true);
                
            }
            
            
        }
        
        System.out.println(game.winner());
    }
}
