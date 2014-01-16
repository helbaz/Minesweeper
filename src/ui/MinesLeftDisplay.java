//********************************************
// The display for the number of unflagged mines left
// Created by Michael Seymour
// Created on 18 September 2011
//********************************************

package ui;

/**
 * The display for the number of unflagged mines left
 */
public class MinesLeftDisplay extends RedIntDisplay
{
    /**
     * Holds the number of unflagged mines left on the grid
     */
    private int numMinesLeft;
    
    /**
     * Constructor, creates a new counter for the unflagged mines left
     */
    public MinesLeftDisplay()
    {
        this.setDisplayNum(0);
    }
    
    /**
     * Sets the number of mines left
     * 
     * @param _numMinesLeft the number of mines left
     */
    public void setNumMinesLeft(final int _numMinesLeft)
    {
        this.numMinesLeft = _numMinesLeft;
        this.setDisplayNum(this.numMinesLeft);
    }
     
    /**
     * Increases the number of mines left by 1
     */
    public void plusOne()
    {
        this.numMinesLeft++;
        this.setDisplayNum(this.numMinesLeft);
    }
    
    /**
     * Decreases the number of mines left by 1
     */
    public void minusOne()
    {
        this.numMinesLeft--;
        this.setDisplayNum(this.numMinesLeft);
    }
}
