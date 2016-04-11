package ch.epfl.xblast.server.debug;

import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;

public final class GameStatePrinter {

    /*
     * static String red = "\u001b[41m"; static String black = "\u001b[40m";
     * static String green = "\u001b[42m"; static String yellow = "\u001b[43m";
     * static String blue = "\u001b[44m"; static String magenta = "\u001b[45m";
     * static String cyan = "\u001b[46m"; static String white = "\u001b[47m";
     * static String std = "\u001b[m";
     */

    private GameStatePrinter() {
    }

    public static void printGameState(GameState s) {
        List<Player> ps = s.players();
        Board board = s.board();

        for (int y = 0; y < Cell.ROWS; ++y) {
            xLoop: for (int x = 0; x < Cell.COLUMNS; ++x) {
                Cell c = new Cell(x, y);
                for (Cell cell : s.blastedCells()) {
                    if (cell.equals(c)) {
                        System.out.print("**");
                        continue xLoop;
                    }
                }
                for (Cell cell : s.bombedCells().keySet()) {
                    if (cell.equals(c)) {
                        System.out.print("òò");
                        continue xLoop;
                    }
                }
                for (Player p : ps) {
                    if (p.position().containingCell().equals(c)) {
                        System.out.print(stringForPlayer(p));
                        continue xLoop;
                    }
                }
                Block b = board.blockAt(c);
                System.out.print(stringForBlock(b));
            }
            if (y / 3 <= 3) {
                System.out.print(stringForStats(ps.get(y / 3), y % 3));
            }
            System.out.println();
        }

        System.out.println(printThings(s));

        System.out.println();
    }

    private static String stringForPlayer(Player p) {
        StringBuilder b = new StringBuilder();
        b.append(p.id().ordinal() + 1);
        switch (p.direction()) {
        case N:
            b.append('^');
            break;
        case E:
            b.append('>');
            break;
        case S:
            b.append('v');
            break;
        case W:
            b.append('<');
            break;
        }
        return b.toString();
    }

    private static String stringForBlock(Block b) {
        switch (b) {
        case FREE:
            return "  ";
        case INDESTRUCTIBLE_WALL:
            return "##";
        case DESTRUCTIBLE_WALL:
            return "??";
        case CRUMBLING_WALL:
            return "¿¿";
        case BONUS_BOMB:
            return "+b";
        case BONUS_RANGE:
            return "+r";
        default:
            throw new Error();
        }
    }

    private static String stringForStats(Player p, int j) {
        switch (j) {
        case 0:
            return "J" + (p.id().ordinal() + 1) + " : " + p.lives() + " vies ("
                    + p.lifeState().state() + ")";
        case 1:
            return "\t bombes max : " + p.maxBombs() + ", portée : "
                    + p.bombRange();
        case 2:
            Cell c = p.position().containingCell();
            return "\t position : " + c + " ± "
                    + p.position().distanceToCentral() + " sous case : "
                    + p.position() + " sous case centrale : "
                    + SubCell.centralSubCellOf(c);
        default:
            throw new Error();
        }
    }

    private static String printThings(GameState s) {
        return "Bombes : " + s.bombedCells() + " -- BlastedCells : "
                + s.blastedCells();
    }
}
