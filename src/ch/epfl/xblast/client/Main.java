package ch.epfl.xblast.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public class Main {

    public final static int NB_MAX_BYTES = 410;

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
        channel.configureBlocking(false);

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

        channel.configureBlocking(true);
        channel.bind(address);
        buffer = ByteBuffer.allocate(NB_MAX_BYTES);
        channel.receive(buffer);
        while (channel.receive(buffer) != null) {
            
            PlayerID id = PlayerID.values()[buffer.get(0)];
            byte[] bytes = buffer.array();
            List<Byte> dgt = new ArrayList<>(Arrays.stream(bytes).boxed().collect(Collectors.toList()));
            XBlastComponent xbc = new XBlastComponent();
            
            
            
            

        }

    }

}
