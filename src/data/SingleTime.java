//********************************************
// A single time, used with the BestTimes class
// Created by Michael Seymour
// Created on 18 September 2011
//********************************************

package data;

/**
 * A single time, used with the BestTimes class
 */
public class SingleTime
{
    private String name;
    /**
     * Returns the name of the player who got this time
     * 
     * @return the name of the player who got this time
     */
    public String getName() {return this.name;}
    
    private int time;
    /**
     * Returns the time this player achieved
     * 
     * @return the time this player achieved
     */
    public int getTime() {return this.time;}
    
    /**
     * Constructor, creates a new time object
     * 
     * @param _name the name of the player who got this time
     * @param _time the time this player achieved
     */
    public SingleTime(final String _name, final int _time)
    {
        this.name = _name;
        this.time = _time;
    }
    
    /**
     * Determines if this time is faster than the given time
     * 
     * @param otherTime the other time to compare with
     * @return true if this time is faster, false otherwise
     */
    public boolean isLessTime(final SingleTime otherTime)
    {
        if (this.time < otherTime.getTime())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
