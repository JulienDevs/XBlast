package ch.epfl.xblast.server.debug;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.synth.SynthSeparatorUI;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;

public class WindowPrinter implements Runnable {
    private final static Block __ = Block.FREE;
    private final static Block XX = Block.INDESTRUCTIBLE_WALL;
    private final static Block xx = Block.DESTRUCTIBLE_WALL;
    private final static List<List<Block>> blockList = Arrays.asList(
            Arrays.asList(__, __, __, __, __, xx, __),
            Arrays.asList(__, XX, xx, XX, xx, XX, xx),
            Arrays.asList(__, xx, __, __, __, xx, __),
            Arrays.asList(xx, XX, __, XX, XX, XX, XX),
            Arrays.asList(__, xx, __, xx, __, __, __),
            Arrays.asList(xx, XX, xx, XX, xx, XX, __));
    private final static List<Player> players = Arrays.asList(
            new Player(PlayerID.PLAYER_1, 3, new Cell(13, 11), 3, 3),
            new Player(PlayerID.PLAYER_2, 3, new Cell(1, 11), 3, 3),
            new Player(PlayerID.PLAYER_3, 3, new Cell(13, 1), 3, 3),
            new Player(PlayerID.PLAYER_4, 3, new Cell(1, 1), 3, 3));
    private final JFrame window;
    private final JPanel container;
    private GameState gameState;
    private KeyBoard keyb;

    public WindowPrinter() {
        window = new JFrame("Xblast 2016");
        container = new JPanel();
        gameState = new GameState(Board.ofQuadrantNWBlocksWalled(blockList),
                players);
        keyb = new KeyBoard();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setLocation(0, 0);
        window.setVisible(true);
        window.setContentPane(container);
        window.setSize(new Dimension(1920, 1080));
        window.addKeyListener(keyb);
    }

    private void paint() {
        Graphics g = container.getGraphics();
        for (Cell c : Cell.ROW_MAJOR_ORDER) {
            String path = null;
            switch (gameState.board().blockAt(c)) {
            case BONUS_BOMB:
                path = "img/BONUS_BOMB.png";
                break;
            case BONUS_RANGE:
                path = "img/BONUS_RANGE.png";
                break;
            case CRUMBLING_WALL:
                path = "img/CRUMBLING_WALL.png";
                break;
            case DESTRUCTIBLE_WALL:
                path = "img/DESTRUCTIBLE_WALL.png";
                break;
            case FREE:
                path = "img/FREE.png";
                break;
            case INDESTRUCTIBLE_WALL:
                path = "img/INDESTRUCTIBLE_WALL.png";
                break;
            }
            try {
                Image img = ImageIO.read(new File(path));
                g.drawImage(img, c.x() * 64, c.y() * 48, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Player p : gameState.players()) {
            if (p.isAlive()) {
                g.setColor(Color.BLUE);
            } else {
                g.setColor(Color.ORANGE);
            }

            g.fillRect((p.position().x() - 8) * 4, (p.position().y() - 8) * 3,
                    64, 48);
        }
        for (Cell c : gameState.bombedCells().keySet()) {
            g.setColor(Color.RED);
            g.fillRect(c.x() * 64, c.y() * 48, 64, 48);
        }
        for (Cell c : gameState.blastedCells()) {
            g.setColor(Color.GREEN);
            g.fillRect(c.x() * 64, c.y() * 48, 64, 48);
        }
        g.setColor(container.getBackground());
        g.fillRect(1080, 0, 500, 500);
        g.setColor(Color.BLACK);
        g.drawString("" + gameState.ticks(), 1080, 64);

        g.setColor(container.getBackground());
        g.fillRect(990, 0, 1000, 1000);

        g.setColor(Color.BLUE);
        g.setFont(new Font("Lucida", Font.PLAIN, 15));

        for (Player p : gameState.alivePlayers()) {
            for (int i = 0; i < 4; i++) {
                g.drawString(getInfosPlayers(p, i), 970,
                        (i * 20) + (p.id().ordinal() + 1) * 100);
            }
        }
    }

    @Override
    public void run() {
        while (!gameState.isGameOver()) {

            System.out.println(gameState.ticks());
            paint();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Map<PlayerID, Optional<Direction>> map = new HashMap<>();
            if (keyb.getDirection1() != null) {
                if (keyb.getDirection1().isPresent()) {
                    map.put(PlayerID.PLAYER_1, keyb.getDirection1());
                } else {
                    map.put(PlayerID.PLAYER_1, Optional.empty());
                }
            }
            if (keyb.getDirection2() != null) {
                if (keyb.getDirection2().isPresent()) {
                    map.put(PlayerID.PLAYER_2, keyb.getDirection2());
                } else {
                    map.put(PlayerID.PLAYER_2, Optional.empty());
                }
            }
            Set<PlayerID> set = new HashSet<>();
            if (keyb.bomb1() == true) {
                set.add(PlayerID.PLAYER_1);
            }
            if (keyb.bomb2() == true) {
                set.add(PlayerID.PLAYER_2);
            }
            gameState = gameState.next(map, set);
            keyb.reset();
        }
    }

    private String getInfosPlayers(Player p, int i) {
        Cell c = p.position().containingCell();
        switch (i) {
        case 0:
            return "J" + (p.id().ordinal() + 1) + " : " + p.lives() + " vies ("
                    + p.lifeState().state() + ")";
        case 1:
            return "Bombes max : " + p.maxBombs() + ", portée : "
                    + p.bombRange() + ", direction empruntée : "
                    + p.direction();
        case 2:
            return "Position : " + c + " ± " + p.position().distanceToCentral()
                    + " sous case : " + p.position();
        case 3:
            return "Sous case centrale : " + SubCell.centralSubCellOf(c)
                    + ", distance to central : "
                    + p.position().distanceToCentral();
        default:
            throw new Error();
        }
    }

    public class KeyBoard implements KeyListener {
        private Optional<Direction> pressed2;
        private Optional<Direction> pressed1;
        private boolean bomb1 = false;
        private boolean bomb2 = false;

        @Override
        public void keyPressed(KeyEvent arg0) {
            switch (arg0.getKeyCode()) {
            case KeyEvent.VK_UP:
                pressed1 = Optional.of(Direction.N);
                break;
            case KeyEvent.VK_RIGHT:
                pressed1 = Optional.of(Direction.E);
                break;
            case KeyEvent.VK_DOWN:
                pressed1 = Optional.of(Direction.S);
                break;
            case KeyEvent.VK_LEFT:
                pressed1 = Optional.of(Direction.W);
                break;
            case KeyEvent.VK_SPACE:
                bomb1 = true;
                break;
            case KeyEvent.VK_CONTROL:
                pressed1 = Optional.empty();
                break;
            case KeyEvent.VK_W:
                pressed2 = Optional.of(Direction.N);
                break;
            case KeyEvent.VK_D:
                pressed2 = Optional.of(Direction.E);
                break;
            case KeyEvent.VK_S:
                pressed2 = Optional.of(Direction.S);
                break;
            case KeyEvent.VK_A:
                pressed2 = Optional.of(Direction.W);
                break;
            case KeyEvent.VK_ALT:
                pressed2 = Optional.empty();
                break;
            case KeyEvent.VK_SHIFT:
                bomb2 = true;
                break;
            default:
                pressed1 = null;
                pressed2 = null;
            }
        }

        public Optional<Direction> getDirection1() {
            return pressed1;
        }

        public Optional<Direction> getDirection2() {
            return pressed2;
        }

        public boolean bomb1() {
            return bomb1;
        }

        public boolean bomb2() {
            return bomb2;
        }

        public void reset() {
            pressed1 = null;
            bomb1 = false;
            pressed2 = null;
            bomb2 = false;
        }

        @Override
        public void keyReleased(KeyEvent arg0) {
        }

        @Override
        public void keyTyped(KeyEvent arg0) {
        }

    }
}