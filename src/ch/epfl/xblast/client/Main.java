package ch.epfl.xblast.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import ch.epfl.xblast.PlayerAction;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public class Main {

    /**
     * @param args
     * @throws IOException
     * @throws InterruptedException
     *             - when the thread is interrupted while it's sleeping
     */
    public static void main(String[] args)
            throws IOException, InterruptedException {
        GameState game = null;
        
        DatagramChannel channel = DatagramChannel
                .open(StandardProtocolFamily.INET);
        channel.configureBlocking(true);

        SocketAddress address = new InetSocketAddress(
                (args == null || args[0] == null || args[0].length() == 0)
                        ? "localhost" : args[0],
                2016);
        ByteBuffer buffer;

        buffer = ByteBuffer.allocate(1);

        do {
            buffer.put((byte) PlayerAction.JOIN_GAME.ordinal());
            buffer.flip();

            channel.send(buffer, address);

            Thread.sleep(1000L);
        } while (channel.receive(buffer) == null);
        
        
    }

}
