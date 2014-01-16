//********************************************
// Handles viewing/saving/resetting the best times
// Created by Michael Seymour
// Created on 18 September 2011
//********************************************

package impl;

import java.io.*;
import java.util.ArrayList;
import data.*;
import javax.swing.JOptionPane;
import ui.BestTimesDialog;

/**
 * Handles viewing/saving/resetting the best times
 */
public class BestTimes
{
    private Minesweeper main;
    /**
     * Returns the main Minesweeper object
     * 
     * @return the main Minesweeper object
     */
    public Minesweeper getMainFrame() {return main;}
    
    /**
     * Path where the best times are stored
     */
    private String filename;
    
    private ArrayList bestTimes = new ArrayList();
    /**
     * Returns the current best times
     * 
     * @return the current best times
     */
    public ArrayList getBestTimes() {return this.bestTimes;}
    
    /**
     * Constructor, creates a new best times handler
     * 
     * @param _main the main Minesweeper object
     * @param _filename the file path to store and retrieve the best times
     */
    public BestTimes(final Minesweeper _main, final String _filename)
    {
        this.main = _main;
        this.filename = _filename;
        
        try
        {
            this.readAllTimesFromFile();
        } 
        catch (Exception Ex) //if file not found or can't read file
        {
            bestTimes.clear();
            for (int i=0; i<3; i++)
            {
                bestTimes.add(new SingleTime("Anonymous", 999));
            }
        }
    }
    
    /**
     * Determines if the given time is the fastest for the given difficulty
     * 
     * @param time the new time
     * @param difficulty the difficulty the time was achieved for
     * @return true if the time is a new best, false otherwise
     */
    public boolean isNewBestTime(final int time, final int difficulty)
    {
        SingleTime newTime = new SingleTime("", time);

        if (difficulty == main.CUSTOM)
        {
            return false;
        }

        if (newTime.isLessTime( (SingleTime)this.bestTimes.get(difficulty) ))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Adds the given time to the best times, if the time is faster
     * 
     * @param name the name of the player who achieved the time
     * @param time the time the player achieved
     * @param difficulty the difficulty the time was achieved for
     */
    public void addTime(final String name, final int time, final int difficulty)
    {
        if (isNewBestTime(time, difficulty))
        {
            SingleTime newTime = new SingleTime(name, time);
            
            this.bestTimes.set(difficulty, newTime);
        }
        
        try
        {
            writeAllTimesToFile();
        } 
        catch(Exception e)
        {
            // Alert the user an error occured
            JOptionPane.showMessageDialog(main,
                "Unable to save new best time to file.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Reads all the best times from a file
     * 
     * @throws FileNotFoundException if the file containing the best times does not exist
     * @throws IOException if there was an error reading the best times from file
     */
    private void readAllTimesFromFile() throws FileNotFoundException, IOException
    {
        DataInputStream in = new DataInputStream(new FileInputStream(this.filename));
        
        String name = "";
        int time = 0;
        
        boolean eof = false;
        while ( ! eof)
        {
            try
            {
                name = in.readUTF();
                time = in.readInt();
                this.bestTimes.add(new SingleTime(name, time));
            }
            catch (EOFException eofEx) {eof = true;}          
        }
        
        in.close();
    }
    
    /**
     * Writes all of the best times to a file
     * 
     * @throws FileNotFoundException if the file containing the best times does not exist
     * @throws IOException if there was an error reading the best times from file
     */
    private void writeAllTimesToFile() throws FileNotFoundException, IOException
    {
        DataOutputStream out = new DataOutputStream(new FileOutputStream(this.filename));
        
        for (int i=0; i<this.bestTimes.size(); i++)
        {
            out.writeUTF(((SingleTime)this.bestTimes.get(i)).getName());
            out.writeInt(((SingleTime)this.bestTimes.get(i)).getTime());
        }
        
        out.close();
    }
    
    /**
     * Sets all the best times to 999 by player Anonymous
     */
    public void resetBestTimes()
    {
        this.bestTimes.clear();
        
        for (int i=0; i<3; i++)
        {
            this.bestTimes.add(new SingleTime("Anonymous", 999));
        }
        
        try
        {
            writeAllTimesToFile();
            this.displayBestTimes();
        } 
        catch(Exception e) {}
    }
    
    /**
     * Creates a dialog showing the best times
     */
    public void displayBestTimes()
    {
        new BestTimesDialog(this);
    }
}
