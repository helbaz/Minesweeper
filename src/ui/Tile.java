//********************************************
// The tiles that make up the grid
// Created by Michael Seymour
// Created on 18 September 2011
//********************************************

package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.basic.BasicBorders.ButtonBorder;
import javax.swing.border.MatteBorder;
import javax.swing.*;

/**
 * The tiles that make up the grid
 */
public class Tile extends JLabel implements MouseListener
{
    private int SIZE = 16; //size of the tile in pixels
    private MineGrid grid;
    private boolean mouseDown = false;
    private boolean mouseOver = false;
    private int col;
    private int row;
    private int value; //value of the tile (0-8, -1 is mine)
    private boolean uncovered;
    private boolean flagged;
    
    /**
     * Constructor, creates a new tile
     * 
     * @param _col the column of the new tile
     * @param _row the row of the new tile
     * @param _grid the mine grid that this tile belongs to
     */
    public Tile(final int _col, final int _row, final MineGrid _grid)
    {
        super("", JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);
        this.setHorizontalAlignment(JLabel.CENTER);
        this.grid = _grid;
        this.setStyleDepressed();
        this.setPreferredSize(new Dimension(this.SIZE, this.SIZE));
        this.addMouseListener(this);
        this.col = _col;
        this.row = _row;
        this.setValue(0);
        this.uncovered = false;
        this.flagged = false;
    }
    
    /**
     * Sets the value of this tile
     * 
     * @param _value the value of the tile, from -1 to 8
     */
    public void setValue(final int _value)
    {
        this.value = _value;
    }
    
    /**
     * Returns the value of this tile
     * 
     * @return the value of this tile
     */
    public int getValue()
    {
        return this.value;
    }
    
    /**
     * Returns true if this tile is a mine, false otherwise
     * 
     * @return true if this tile is a mine, false otherwise
     */
    public boolean isMine()
    {
        if (this.value == -1)
            return true;
        else
            return false;
    }
    
    /**
     * Makes this tile a mine
     */
    public void setMine()
    {
        this.value = -1;
    }
    
    /**
     * Returns true if this tile has no neighbouring mines
     * 
     * @return true if this tile has no neighbouring mines
     */
    public boolean noNeighbourMines()
    {
        if (this.value == 0)
            return true;
        else
            return false;
    }
    
    /**
     * Uncovers the value of this tile for the player to see
     */
    public void uncoverValue()
    {
        this.uncovered = true;
        this.setStyleImpressed();
        
        if (this.isMine())
        {
            // BOOOOOOM!!!!
            if ( ! this.grid.getMainFrame().isGameOver())
            {
                this.makeRed(); //make this critical tile red
                this.grid.getMainFrame().lostGame();
            }
            else
            {
                this.setIcon(new ImageIcon(this.grid.getMainFrame().RES_MINE));
            }
        }
        else if (this.value > 0)
        {
            if (this.isFlagged() && this.grid.getMainFrame().isGameOver())
            {
                this.setIcon(new ImageIcon(this.grid.getMainFrame().RES_NOT_MINE));
            }
            else
            {
                switch (this.value)
                {
                    case 1:  setForeground(Color.BLUE);                     break;
                    case 2:  setForeground(Color.GREEN.darker().darker());  break;
                    case 3:  setForeground(Color.RED);                      break;
                    case 4:  setForeground(Color.BLUE.darker().darker());   break;
                    case 5:  setForeground(Color.RED.darker());             break;
                    case 6:  setForeground(Color.CYAN.darker().darker());   break;
                    case 7:  setForeground(Color.BLACK);                    break;
                    case 8:  setForeground(Color.GRAY.darker());            break;
                    default: setForeground(Color.BLACK);                    break;
                }
                
                this.setText(""+this.value);
                
                // If a tile was uncovered using uncoverAdjacentTiles, it should not remain flagged
                if (this.isFlagged())
                        this.toggleFlagged();
            }
        }
    }
    
    /**
     * Returns true if this tile has been uncovered, false otherwise
     * 
     * @return true if this tile has been uncovered, false otherwise
     */
    public boolean isUncovered()
    {
        return this.uncovered;
    }
    
    /**
     * Makes the background of this tile red
     */
    private void makeRed()
    {
        this.setOpaque(true);
        this.setBackground(Color.red);
    }
    
    /**
     * Removes the flag if a tile is flagged and vice versa
     */
    public void toggleFlagged()
    {
        if ( ! this.flagged)
        {
            this.flagged = true;
            this.setIcon(new ImageIcon(this.grid.getMainFrame().RES_FLAG));
            this.grid.getMainFrame().getMinesLeftDisplay().minusOne();
        }
        else
        {
            this.flagged = false;
            this.setIcon(null);
            this.grid.getMainFrame().getMinesLeftDisplay().plusOne();
        }
    }
    
    /**
     * Returns true if this tile is flagged
     * 
     * @return true if this tile is flagged
     */
    public boolean isFlagged()
    {
        return this.flagged;
    }
    
    @Override
    public void mousePressed(MouseEvent e)
    {
        if ( ! grid.getMainFrame().isGameOver())
        {
            this.mouseDown = true;
            this.mouseOver = true;

            if ( ! this.uncovered && SwingUtilities.isLeftMouseButton(e) && ! this.flagged)
            {
                this.setStyleImpressed();
                this.grid.getMainFrame().setSmileyIcon(this.grid.getMainFrame().RES_SMILEY_NERVOUS);
            }
            else if (SwingUtilities.isMiddleMouseButton(e))
            {
                this.grid.getMainFrame().setSmileyIcon(this.grid.getMainFrame().RES_SMILEY_NERVOUS);
            }
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e)
    {
        if ( ! this.grid.getMainFrame().isGameOver())
        {
            this.grid.getMainFrame().setSmileyIcon(this.grid.getMainFrame().RES_SMILEY_NORMAL);
            
            if (this.mouseOver && ! this.uncovered)
            {
                if (SwingUtilities.isLeftMouseButton(e) && ! this.flagged)
                {
                    if ( ! this.grid.areMinesAdded())
                    {
                        this.grid.addMines(this.col, this.row);
                        this.grid.getMainFrame().getTimerDisplay().startTimer();
                    }
                    this.uncoverValue();
                    this.grid.tileUncovered(this.col, this.row);
                    this.grid.checkWon(); //checks if won after tileUncovered, therefore, after a chain possibly found
                }
                else if (SwingUtilities.isRightMouseButton(e))
                {
                    this.toggleFlagged();
                }
            }
            
            if (SwingUtilities.isMiddleMouseButton(e))
            {
                // problem: if middle release not over tile, still presses tile
                this.grid.uncoverAdjacentTiles(this.col, this.row);
                this.grid.checkWon();
            }
            
            this.mouseDown = false;
            this.mouseOver = false; //must go at end of mouseReleased
        }
    }
    
    @Override
    public void mouseExited(MouseEvent e)
    {
        if ( ! grid.getMainFrame().isGameOver())
        {
            if (this.mouseDown)
                this.mouseOver = false;

            if ( ! this.uncovered && this.mouseDown && ! this.flagged)
            {
                this.setStyleDepressed();
            }
        }
    }
    
    @Override
    public void mouseEntered(MouseEvent e)
    {
        if ( ! grid.getMainFrame().isGameOver())
        {
            if (this.mouseDown)
                this.mouseOver = true;

            if ( ! this.uncovered && this.mouseDown && ! this.flagged)
            {
                this.setStyleImpressed();
            }
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {}
    
    /**
     * Makes this tile look impressed
     */
    private void setStyleImpressed()
    {
        this.setBorder(new MatteBorder(
            1, //top
            1, //left
            0, //bottom
            0, //right
            new Color(128,128,128)
        ));
    }
    
    /**
     * Makes this tile look depressed
     */
    private void setStyleDepressed()
    {
        setBorder(new ButtonBorder(
            new Color(128,128,128), //shadow
            new Color(128,128,128), //darkShadow
            Color.white, //highlight
            Color.white //lightHighlight
        ));
    }
}
