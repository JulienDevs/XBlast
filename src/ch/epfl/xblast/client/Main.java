package ch.epfl.xblast.client;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Time;

/**
 * Main of the client. Handles the display of the game state bytes sent by the
 * server and the sends to it the actions of the players.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
public class Main {

    private final static int NB_MAX_BYTES = 410;

    private static SocketAddress address;
    private static XBlastComponent xbc;

    /**
     * Displays game state using the bytes received from the server. Sends back
     * the actions of the players.
     * 
     * @param args
     *            - address of the receiver (if none, then it is the localhost)
     * @throws IOException
     * @throws InterruptedException
     * @throws InvocationTargetException
     */
    public static void main(String[] args) throws IOException,
            InterruptedException, InvocationTargetException {

        DatagramChannel channel = DatagramChannel
                .open(StandardProtocolFamily.INET);

        channel.configureBlocking(false);
        address = new InetSocketAddress(
                (args == null || args.length == 0 || args[0] == null
                        || args[0].length() == 0) ? "localhost" : args[0],
                2016);

        ByteBuffer buffer;

        buffer = ByteBuffer.allocate(1);

        do {
            buffer.put((byte) PlayerAction.JOIN_GAME.ordinal());
            buffer.flip();

            channel.send(buffer, address);
            buffer.clear();
            Thread.sleep(Time.MS_PER_S);
        } while (channel.receive(buffer) == null);

        channel.configureBlocking(true);

        buffer = ByteBuffer.allocate(NB_MAX_BYTES);

        SwingUtilities.invokeAndWait(() -> createUI(channel));
        xbc.requestFocusInWindow();

        while (channel.isOpen()) {
            channel.receive(buffer);

            PlayerID id = PlayerID.values()[buffer.get(0)];

            List<Byte> bytesList = new ArrayList<>();
            for (int i = 1; i < NB_MAX_BYTES; ++i) {
                bytesList.add(buffer.get(i));
            }

            GameState game = GameStateDeserializer
                    .deserializeGameState(bytesList);
            xbc.setGameState(game, id);
            buffer.clear();
        }
    }

    private static void createUI(DatagramChannel channel) {

        Map<Integer, PlayerAction> kb = new HashMap<>();
        kb.put(KeyEvent.VK_UP, PlayerAction.MOVE_N);
        kb.put(KeyEvent.VK_RIGHT, PlayerAction.MOVE_E);
        kb.put(KeyEvent.VK_DOWN, PlayerAction.MOVE_S);
        kb.put(KeyEvent.VK_LEFT, PlayerAction.MOVE_W);
        kb.put(KeyEvent.VK_SHIFT, PlayerAction.STOP);
        kb.put(KeyEvent.VK_SPACE, PlayerAction.DROP_BOMB);
        xbc = new XBlastComponent();

        Consumer<PlayerAction> c = x -> {
            ByteBuffer buffer2 = ByteBuffer.allocate(1);
            buffer2.put((byte) x.ordinal());
            buffer2.flip();

            try {
                channel.send(buffer2, address);
            } catch (Exception e) {
                e.printStackTrace();
            }

        };

        xbc.addKeyListener(new KeyboardEventHandler(kb, c));

        JFrame frames = new JFrame("XBC");
        frames.setContentPane(xbc);
        frames.setUndecorated(true);
        frames.setResizable(false);
        frames.setVisible(true);
        frames.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frames.pack();
        frames.setLocationRelativeTo(null);

    }

}
