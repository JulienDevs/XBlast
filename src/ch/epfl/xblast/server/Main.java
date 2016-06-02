package ch.epfl.xblast.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerAction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.Time;

/**
 * Main of the server. Handles the computation of the next game state using the
 * actions of the players sent by the clients. Sends the game state to the
 * client.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */

public final class Main {

    private static final int DEFAULT_NB_OF_CLIENTS = 4;
    private static Map<SocketAddress, PlayerID> ips = new HashMap<>();
    private static DatagramChannel channel;

    /**
     * Computes the next game state using the actions of the players sent by the
     * clients. Sends the game state back to the clients.
     * 
     * @param args
     *            - number of expected clients (4 if none is given)
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        GameState game = Level.DEFAULT_LEVEL.gameState();
        BoardPainter bPainter = Level.DEFAULT_LEVEL.boardPainter();
        int expectedClients = DEFAULT_NB_OF_CLIENTS;
        ByteBuffer buffer;

        try {
            if (args != null && args.length != 0) {
                if (Integer.parseInt(args[0]) > 0
                        && Integer.parseInt(args[0]) < DEFAULT_NB_OF_CLIENTS) {
                    expectedClients = Integer.parseInt(args[0]);
                }
            }
        } catch (NumberFormatException e) {
        }

        channel = DatagramChannel.open(StandardProtocolFamily.INET);
        channel.bind(new InetSocketAddress(2016));

        buffer = ByteBuffer.allocate(1);

        SocketAddress senderAddress;
        while (ips.size() < expectedClients) {
            senderAddress = channel.receive(buffer);
            if (buffer.get(0) == PlayerAction.JOIN_GAME.ordinal()
                    && !ips.containsKey(senderAddress))
                ips.put(senderAddress, PlayerID.values()[ips.size()]);
        }

        channel.configureBlocking(false);

        long initialTime = System.nanoTime();

        while (!game.isGameOver()) {
            List<Byte> serializedGameState = GameStateSerializer
                    .serialize(bPainter, game);

            ByteBuffer gs = ByteBuffer.allocate(serializedGameState.size() + 1);
            gs.put((byte) 0);
            for (byte b : serializedGameState) {
                gs.put(b);
            }

            for (SocketAddress s : ips.keySet()) {
                gs.put(0, (byte) ips.get(s).ordinal());
                gs.rewind();
                channel.send(gs, s);
            }

            ByteBuffer playerActions = ByteBuffer.allocate(1);

            Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
            Set<PlayerID> bombDropEvents = new HashSet<>();

            SocketAddress receiverAddress;

            while ((receiverAddress = channel.receive(playerActions)) != null) {
                switch (PlayerAction.values()[playerActions.get(0)]) {
                case MOVE_N:
                    speedChangeEvents.put(ips.get(receiverAddress),
                            Optional.of(Direction.N));
                    break;
                case MOVE_E:
                    speedChangeEvents.put(ips.get(receiverAddress),
                            Optional.of(Direction.E));
                    break;
                case MOVE_S:
                    speedChangeEvents.put(ips.get(receiverAddress),
                            Optional.of(Direction.S));
                    break;
                case MOVE_W:
                    speedChangeEvents.put(ips.get(receiverAddress),
                            Optional.of(Direction.W));
                    break;
                case STOP:
                    speedChangeEvents.put(ips.get(receiverAddress),
                            Optional.empty());
                    break;
                case DROP_BOMB:
                    bombDropEvents.add(ips.get(receiverAddress));
                    break;
                default:
                    break;
                }
                playerActions = ByteBuffer.allocate(1);
            }

            game = game.next(speedChangeEvents, bombDropEvents);
            long actualTime = System.nanoTime();
            long timeSinceLastTick = actualTime - initialTime;
            long expectedTimeUntilNextTick = (long) (game.ticks() + 1)
                    * Ticks.TICK_NANOSECOND_DURATION;
            if (expectedTimeUntilNextTick > timeSinceLastTick) {
                try {
                    long timeUntilNextTick = expectedTimeUntilNextTick
                            - timeSinceLastTick;
                    Thread.sleep(
                            timeUntilNextTick * Time.MS_PER_S / Time.NS_PER_S,
                            (int) (timeUntilNextTick
                                    % (Time.NS_PER_S / Time.MS_PER_S)));
                } catch (InterruptedException e) {
                }
            }
        }

        System.out.println(game.winner());
    }
}
