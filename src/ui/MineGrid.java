//********************************************
// Displays and handles the main grid for the game
// Created by Michael Seymour
// Created on 18 September 2011
//********************************************

package ui;

import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicBorders.ButtonBorder;
import javax.swing.BorderFactory;
import java.awt.*;
import impl.Minesweeper;

/**
 * Displays and handles the main grid for the game
 */
public class MineGrid extends JPanel
{
    private Minesweeper main;
    /**
     * Returns the main frame for the game
     * 
     * @return the main frame for the game
     */
    public Minesweeper getMainFrame() {return main;}
    
    private int width;
    /**
     * Returns the current grid's width
     * 
     * @return the current grid's width
     */
    public int getGridWidth() {return this.width;}
    
    private int height;
    /**
     * Returns the current grid's height
     * 
     * @return the current grid's height
     */
    public int getGridHeight() {return this.height;}
    
    private int numMines;
    /**
     * Returns the number of mines in the current grid
     * 
     * @return the number of mines in the current grid
     */
    public int getNumMines() {return this.numMines;}
    
    /**
     * The array of tiles that make up the grid
     */
    private Tile[][] tile;
    
    private boolean minesAdded;
    /**
     * Returns true if the mines have been added to the grid, false otherwise
     * 
     * @return true if the mines have been added to the grid, false otherwise
     */
    public boolean areMinesAdded() {return minesAdded;}
    
    /**
     * Constructor, creates a new mine grid
     * 
     * @param _main the main Minesweeper object
     */
    public MineGrid(final Minesweeper _main)
    {
        this.main = _main;
        setBackground(new Color(192,192,192));
        this.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(8,8,8,8),
            new ButtonBorder(
                Color.white, //shadow
                Color.white, //darkShadow
                new Color(128,128,128), //highlight
                new Color(128,128,128) //lightHighlight
            ) 
        ));
    }
    
    /**
     * Creates the tiles for the grid, and sets up the grid
     * 
     * @param _width the width of the grid
     * @param _height the height of the grid
     * @param _numMines the number of mines on the grid
     */
    public void setGrid(final int _width, final int _height, final int _numMines)
    {
        setVisible(false);
        
        this.width = _width;
        this.height = _height;
        this.numMines = _numMines;
        
        this.minesAdded = false;
        
        this.setLayout(new GridLayout(/*rows*/this.height, /*cols*/this.width, 0, 0));
        
        // Removes all the tiles from this mine grid
        this.removeAll();
        
        this.tile = new Tile[this.width][this.height];
        
        for (int i=0; i<this.height; i++) //must put height first so it adds row by row, not col by col
        {
            for (int j=0; j<this.width; j++)
            {
                this.tile[j][i] = new Tile(j, i, this);
                add(this.tile[j][i]);
            }
        }
        
        setVisible(true);
    }
    
    /**
     * Adds all mines to this grid
     * 
     * @param col the column of the very first tile uncovered
     * @param row the row of the very first tile uncovered
     */
    public void addMines(final int col, final int row)
    {
        this.minesAdded = true;
        
        // If more mines than tiles, set num mines to one less than num tiles
        if (this.numMines >= this.width*this.height)
        {
            this.numMines = this.width*this.height - 1;
        }
        
        int x, y; //position of next mine
        int i = 0;
        while (i < this.numMines)
        {
            // Generate a random position for the mine
            x = Math.round((float) Math.random() * (this.width - 1));
            y = Math.round((float) Math.random() * (this.height - 1));
            
            // If the tile isn't already a mine or the first uncovered tile
            if ( ! this.tile[x][y].isMine() && ! (x==col && y==row))
            {
                this.tile[x][y].setMine();
                i++;
            }
        }
        
        this.setTileValues();
    }
    
    /**
     * Sets the value of each tile based on its surrounding mines
     */
    private void setTileValues()
    {
        // For each tile in the grid
        for (int i=0; i<this.width; i++) {
        for (int j=0; j<this.height; j++)
        {
            // Don't bother checking if bomb
            if (this.tile[i][j].isMine())
            {
                continue;
            }
            
            int neighbourMines = 0; //number of neighbouring mines
            
            // For each surrounding tile
            for (int k=i-1; k<=i+1; k++) {
            for (int l=j-1; l<=j+1; l++)
            {
                if ( ! (k<0 || l<0 || k>=this.width || l>=this.height) //neighbour tile not out of grid
                     && ! (k==i && l==j) //tile not the center (original) tile itself
                     && this.tile[k][l].isMine() ) //neighbour tile is a mine
                {
                    neighbourMines++;
                }
            }
            }
            
            this.tile[i][j].setValue(neighbourMines);
        }
        }
    }
    
    /**
     * Runs when a tile has been uncovered. If the tile has no neighbouring 
     * mines, all adjacent tiles are uncovered as well.
     * 
     * @param col the column of the tile that was uncovered
     * @param row the row of the tile that was uncovered
     */
    public void tileUncovered(int col, int row)
    {
        this.setVisible(false);
        if ( ! tile[col][row].isMine() && tile[col][row].noNeighbourMines())
        {
            // For each surrounding tile
            for (int i=col-1; i<=col+1; i++) {
            for (int j=row-1; j<=row+1; j++)
            {
                if ( ! (i<0 || j<0 || i>=this.width || j>=this.height) //neighbour tile not out of grid
                     && ! (i==col && j==row)) //tile not the center (original) tile itself
                {
                    if ( ! tile[i][j].isUncovered())
                    {
                        if (tile[i][j].isFlagged())
                            tile[i][j].toggleFlagged();
                        tile[i][j].uncoverValue();
                        this.tileUncovered(i, j);
                    }
                }
            }
            }
        }
        this.setVisible(true);
    }
    
    /**
     * Checks if the Minesweeper game, in its current state, has been won 
     * 
     * @return true if all non-mine blocks have been uncovered, false otherwise
     */
    public boolean checkWon()
    {
        // For each tile in the grid
        for (int i=0; i<this.width; i++) {
        for (int j=0; j<this.height; j++)
        {
            // If the tile isn't a mine and hasn't been uncovered
            if ( ! tile[i][j].isMine() && ! tile[i][j].isUncovered())
                return false;
        }
        }
        
        this.getMainFrame().wonGame();
        return true;
    }
    
    /**
     * Shows the mines and incorrectly flagged mines
     */
    public void showMines()
    {
        this.setVisible(false); //ensures mines appear simultaneously
        
        // For each tile in the grid
        for (int i=0; i<this.width; i++) {
        for (int j=0; j<this.height; j++)
        {
            if ((tile[i][j].isMine() && ! tile[i][j].isFlagged()) //if tile is an unflagged mine
                || ( ! tile[i][j].isMine() && tile[i][j].isFlagged())) //if tile is flagged and not a mine
            {
                tile[i][j].uncoverValue();
            }
        }
        }
        
        this.setVisible(true);
    }
    
    /**
     * Uncovers the adjacent tiles to the tile given
     * 
     * @param col the column of the given tile
     * @param row the row of the given tile
     */
    public void uncoverAdjacentTiles(final int col, final int row)
    {
        this.setVisible(false); //ensures tiles uncover simultaneously
        
        if (tile[col][row].isUncovered()
            && tile[col][row].getValue() == this.numAdjacentFlags(col, row))
        {
            // For each surrounding tile
            for (int i=col-1; i<=col+1; i++) {
            for (int j=row-1; j<=row+1; j++)
            {
                if ( ! (i<0 || j<0 || i>=this.width || j>=this.height) //neighbour tile not out of grid
                    && ! (i==col && j==row)) //tile not the center (original) tile itself
                {
                    if ( ! tile[i][j].isUncovered() && ! tile[i][j].isFlagged())
                    {
                        tile[i][j].uncoverValue();
                        this.tileUncovered(i, j);
                    }
                }
            }
            }
        }
        
        this.setVisible(true);
    }
    
    /**
     * Calculates and returns the number of adjacent flags to a given tile
     * 
     * @param col the column of the given tile
     * @param row the row of the given tile
     * @return the number of adjacent flags
     */
    private int numAdjacentFlags(final int col, final int row)
    {
        int neighbourFlags = 0;
        
        // For each surrounding tile
        for (int i=col-1; i<=col+1; i++) {
        for (int j=row-1; j<=row+1; j++)
        {
            if ( ! (i<0 || j<0 || i>=this.width || j>=this.height) //neighbour tile not out of grid
                && ! (i==col && j==row)) //tile not the center (original) tile itself
            {
                if (tile[i][j].isFlagged())
                {
                    neighbourFlags++;
                }
            }
        }
        }
        
        return neighbourFlags;
    }
}
