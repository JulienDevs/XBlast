package ch.epfl.xblast.server.debug;

import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;

public final class GameStatePrinter {
    private GameStatePrinter() {}

    public static void printGameState(GameState s) {
        List<Player> ps = s.alivePlayers();
        Board board = s.board();

        for (int y = 0; y < Cell.ROWS; ++y) {
            xLoop: for (int x = 0; x < Cell.COLUMNS; ++x) {
                Cell c = new Cell(x, y);
                for (Player p: ps) {
                    if (p.position().containingCell().equals(c)) {
                        System.out.print(stringForPlayer(p));
                        continue xLoop;
                    }
                }
                Block b = board.blockAt(c);
                System.out.print(stringForBlock(b));
            }
            System.out.println();
        }
    }

    private static String stringForPlayer(Player p) {
        StringBuilder b = new StringBuilder();
        b.append("\u001b[106m");
        b.append(p.id().ordinal() + 1);
        switch (p.direction()) {
        case N: b.append('↑'); break;
        case E: b.append('→'); break;
        case S: b.append('↓'); break;
        case W: b.append('←'); break;
        }
        return b.toString();
    }

    private static String stringForBlock(Block b) {
        switch (b) {
        case FREE: return "\u001b[43m  \u001b[49m";
        case INDESTRUCTIBLE_WALL: return "\u001b[40m  \u001b[49m";
        case DESTRUCTIBLE_WALL: return "\u001b[40m\u001b[97m??\u001b[39m\u001b[49m";
        case CRUMBLING_WALL: return "\u001b[40m\u001b[97m¿¿\u001b[39m\u001b[49m";
        case BONUS_BOMB: return "+b";
        case BONUS_RANGE: return "+r";
        default: throw new Error();
        }
    }

}
