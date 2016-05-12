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
        int expectedClients = 1;
        ByteBuffer buffer;
        Map<SocketAddress, PlayerID> ips = new HashMap<>();

        if (args.length != 0)
            expectedClients = Integer.parseInt(args[0]);

        DatagramChannel channel = DatagramChannel
                .open(StandardProtocolFamily.INET);
        channel.bind(new InetSocketAddress(2016));

        buffer = ByteBuffer.allocate(1);
        SocketAddress senderAddress;
        while (ips.size() < expectedClients) {
            senderAddress = channel.receive(buffer);
            System.out.println(buffer.get(0));
            if (buffer.get(0) == PlayerAction.JOIN_GAME.ordinal()
                    && !ips.containsKey(senderAddress)) {
                ips.put(senderAddress, PlayerID.values()[ips.size()]);
            }
        }

        channel.configureBlocking(false);
        long initialTime = System.nanoTime();

        while (!game.isGameOver()) {
            List<Byte> serializedGameState = GameStateSerializer
                    .serialize(bPainter, game);

            ByteBuffer gs = ByteBuffer.allocate(serializedGameState.size());
            for (byte b : serializedGameState) {
                gs.put(b);
            }

            for (Map.Entry<SocketAddress, PlayerID> e : ips.entrySet()) {
                buffer = ByteBuffer.allocate(serializedGameState.size() + 1);

                buffer.put((byte) e.getValue().ordinal());
               
                gs.flip();
                buffer.put(gs);

                buffer.flip();
                System.out.println(buffer);
                channel.send(buffer, e.getKey());
                buffer.clear();
            }

//            long timeOfNextTick = initialTime
//                    + (long)game.ticks() * Ticks.TICK_NANOSECOND_DURATION;
//            long remainingTime = timeOfNextTick - System.nanoTime();
//            if(remainingTime > 0){
//                try {
//                    Thread.sleep(remainingTime/1000);
//                } catch (InterruptedException e1) {
//                }
//            }
            long end = System.nanoTime();
            long duration = end - initialTime;
            long minDuration = (long) (game.ticks() + 1)
                    * Ticks.TICK_NANOSECOND_DURATION;
            if (duration < minDuration) {
                try {
                    long sleepDuration = minDuration - duration;
                    Thread.sleep(sleepDuration * Time.MS_PER_S / Time.NS_PER_S,
                            (int) (sleepDuration
                                    % (Time.NS_PER_S / Time.MS_PER_S)));
                } catch (InterruptedException e) {
                }
            }

            ByteBuffer playerActions = ByteBuffer.allocate(1);

            Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
            Set<PlayerID> bombDropEvents = new HashSet<>();

            SocketAddress receiverAddress = channel.receive(playerActions);
            
            System.out.println(playerActions);

            switch (PlayerAction.values()[buffer.get(0)]) {
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
                playerActions = ByteBuffer.allocate(1);
                break;
            }

            game = game.next(speedChangeEvents, bombDropEvents);
        }
        
        System.out.println();
    }
}
