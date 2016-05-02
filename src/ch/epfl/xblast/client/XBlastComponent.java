/**
 * 
 */ 
package ch.epfl.xblast.client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

/**
 * @author Yaron Dibner (257145)
 * @author Julien Malka (259041)
*/
public final class XBlastComponent extends JComponent{
    private final static int W = 960;
    private final static int H = 688;
    private final static int IMAGE_WIDTH = 64;
    private final static int IMAGE_HEIGHT = 48;
    
    private GameState game = null;
    
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(W, H);
    }
    
    @Override
    protected void paintComponent(Graphics g0){
        Graphics2D g = (Graphics2D)g0;
        
        for(int y = 0 ; y < W ; y += IMAGE_HEIGHT){
            for(int x = 0 ; x < H ; x += IMAGE_WIDTH){
                g.drawImage(game.board().get(), x, y, observer)
            }
        }
    }
    
    public void setGameState(GameState game, PlayerID p){
        this.game = game;
        repaint();
    }
}
