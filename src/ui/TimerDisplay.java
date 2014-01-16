//********************************************
// The display for the timer
// Created by Michael Seymour
// Created on 18 September 2011
//********************************************

package ui;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The display for the timer
 */
public class TimerDisplay extends RedIntDisplay
{
    private int numSeconds = -1;
    /**
     * Returns the number of seconds on the timer
     * 
     * @return the number of seconds on the timer
     */
    public int getNumSeconds() {return this.numSeconds;}
    
    private Timer timer;
    
    /**
     * Constructor, creates a new counter for the game time
     */
    public TimerDisplay()
    {
        this.setDisplayNum(0);
    }
    
    /**
     * Starts the timer, incrementing the time by 1 every 1000 milliseconds
     */
    public void startTimer()
    {
        this.timer = new Timer();
        
        TimerTask incTimerTask = new TimerTask()
            {
                @Override
                public void run()
                {
                    numSeconds++;
                    setDisplayNum(numSeconds);
                }
            };
        
        this.timer.scheduleAtFixedRate(incTimerTask, 0, 1000);
    }
    
    /**
     * Stops the timer at its current time
     */
    public void stopTimer()
    {
        this.timer.cancel();
    }
    
    /**
     * Sets the time on the timer back to 0
     */
    public void resetTimer()
    {
        if (this.timer instanceof Timer)
            this.timer.cancel();
        this.numSeconds = -1;
        this.setDisplayNum(0);
    }
}
