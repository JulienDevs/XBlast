package ch.epfl.xblast.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComponent;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.client.GameState.Player;

/**
 * A Swing component that displays the state of a game of XBlast.
 * 
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
 */
@SuppressWarnings("serial")
public final class XBlastComponent extends JComponent {
    private final static int W = 960;
    private final static int H = 688;
    private final static int BLOCK_WIDTH = 64;
    private final static int BLOCK_HEIGHT = 48;
    private final static int SCORE_SIZE = 48;
    private final static int TIME_SIZE = 16;
    private final static List<Integer> SCORE_X_POSITIONS = new ArrayList<>(
            Arrays.asList(96, 240, 768, 912));
    private final static int SCORE_Y_POSITION = 659;

    private GameState game = null;
    private List<PlayerID> playerIds;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(W, H);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        if (game != null) {

            // -------- Board & Explosions --------

            for (Cell c : Cell.ROW_MAJOR_ORDER) {
                int x = c.x() * BLOCK_WIDTH;
                int y = c.y() * BLOCK_HEIGHT;
                int index = c.rowMajorIndex();

                g.drawImage(game.board().get(index), x, y, null);
                g.drawImage(game.explosions().get(index), x, y, null);
            }

            // -------- Score ---------

            int x = 0, y = Cell.ROWS * BLOCK_HEIGHT;

            for (Image i : game.scores()) {
                g.drawImage(i, x, y, null);

                x += SCORE_SIZE % W;
            }

            // -------- Time ----------

            y += SCORE_SIZE;
            x = 0;

            for (Image i : game.time()) {
                g.drawImage(i, x, y, null);

                x += TIME_SIZE % W;
            }

            Font font = new Font("Arial", Font.BOLD, 25);
            g.setColor(Color.WHITE);
            g.setFont(font);
            for (int i = 0; i < playerIds.size(); i++) {
                g.drawString(Integer.toString(game.players().get(i).lives()),
                        SCORE_X_POSITIONS.get(i).intValue(), SCORE_Y_POSITION);
            }

            Comparator<Player> verticalPositionComparator = (p1, p2) -> Integer
                    .compare(p1.position().y(), p2.position().y());
            Comparator<Player> currentPlayerComparator = (p1, p2) -> Integer
                    .compare(playerIds.indexOf(p1.id()),
                            playerIds.indexOf(p2.id()));
            Comparator<Player> playerComparator = verticalPositionComparator
                    .thenComparing(currentPlayerComparator);

            List<Player> players = new ArrayList<>(game.players());

            players.sort(playerComparator);

            for (Player p : players) {
                int xs = 4 * p.position().x() - 24;
                int ys = 3 * p.position().y() - 52;

                g.drawImage(p.image(), xs, ys, null);
            }
        }
    }

    /**
     * Updates the GameState for the display. The player's id is given to
     * resolve the cases where two players stand on top of each other.
     * 
     * @param game
     *            - the new GameState
     * @param p
     *            - the id of the client
     */
    public void setGameState(GameState game, PlayerID p) {
        this.game = game;

        playerIds = Arrays.asList(PlayerID.values());
        Collections.rotate(playerIds, playerIds.size() - p.ordinal() - 1);

        repaint();
    }
}
