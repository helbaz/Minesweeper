//********************************************
// Minesweeper clone
// Created by Michael Seymour
// Created on 18 September 2011
//********************************************

package impl;

import javax.swing.plaf.basic.BasicBorders.ButtonBorder;
import javax.swing.border.Border;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import ui.*;

/**
 * Main class for the Minesweeper game
 */
public class Minesweeper extends JFrame
{
    /**
     * Resource filenames and paths
     */
    public final String RES_SETTINGS       = "res/settings.dat";
    public final String RES_BEST_TIMES     = "res/besttimes.dat";
    public final String RES_GAME_ICON      = "res/res_gameicon.gif";
    public final String RES_SMILEY_NORMAL  = "res/res_smileynormal.gif";
    public final String RES_SMILEY_DEAD    = "res/res_smileydead.gif";
    public final String RES_SMILEY_COOL    = "res/res_smileycool.gif";
    public final String RES_SMILEY_NERVOUS = "res/res_smileynervous.gif";
    public final String RES_FLAG           = "res/res_flag.gif";
    public final String RES_MINE           = "res/res_mine.gif";
    public final String RES_NOT_MINE       = "res/res_notmine.gif";
    
    private MineGrid mineGrid;
    /**
     * Returns an instance of this game's mine grid
     * 
     * @return an instance of this game's mine grid
     */
    public MineGrid getMineGrid() {return this.mineGrid;}
    
    private TimerDisplay timerDisplay;
    /**
     * Returns an instance of this game's display for the timer
     * 
     * @return an instance of this game's display for the timer
     */
    public TimerDisplay getTimerDisplay() {return this.timerDisplay;}
    
    private MinesLeftDisplay minesLeftDisplay;
    /**
     * Returns an instance of this game's display for the number of mines left
     * 
     * @return an instance of this game's display for the number of mines left
     */
    public MinesLeftDisplay getMinesLeftDisplay() {return this.minesLeftDisplay;}
    
    /**
     * Smiley face button used to start a new game
     */
    private JLabel butNewGame;
    
    private BestTimes bestTimes;
    /**
     * Returns an instance of the object that displays and handles the best times
     * 
     * @return an instance of the best times object
     */
    public BestTimes getBestTimesObj() {return this.bestTimes;}
    
    private boolean gameOver;
    /**
     * Checks if the game is over or still in progress
     * 
     * @return true if the current game is over, false otherwise
     */
    public boolean isGameOver() {return gameOver;}
    
    private int difficulty;
    /**
     * Returns the difficulty of the current game
     * 
     * @return the difficulty of the current game
     */
    public int getDifficulty() {return this.difficulty;}
    
    /**
     * Game difficulty constants
     */
    public final int BEGINNER = 0;
    public final int INTERMEDIATE = 1;
    public final int EXPERT = 2;
    public final int CUSTOM = 3;
    
    /**
     * Settings for custom Minesweeper games
     */
    private int customWidth;
    private int customHeight;
    private int customNumMines;
    
    /**
     * Button borders
     */
    private Border borderDepressed;
    private Border borderImpressed;
    
    /**
     * Initiates the Minesweeper game
     * 
     * @param args the command line arguments
     */
    public static void main(final String[] args)
    {
        // Initialises the application
        new Minesweeper();
    }
    
    /**
     * Constructor, creates a new Minesweeper game
     */
    public Minesweeper()
    {
        // Set the window title
        super("Minesweeper"); 
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set the window icon
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(RES_GAME_ICON));
        
        // Create the 2 red integer displays
        this.timerDisplay = new TimerDisplay();
        this.minesLeftDisplay = new MinesLeftDisplay();
        
        // Create a manager for the best times
        this.bestTimes = new BestTimes(this, RES_BEST_TIMES);
        
        // Create the grid containing all the mines
        this.mineGrid = new MineGrid(this);
        
        // Create button borders
        this.borderDepressed = BorderFactory.createCompoundBorder(
            new ButtonBorder(
                new Color(128,128,128), //shadow
                new Color(128,128,128), //darkShadow
                Color.white, //highlight
                Color.white //lightHighlight
            ),
            BorderFactory.createEmptyBorder(1,1,1,1)
        );
        this.borderImpressed = BorderFactory.createCompoundBorder(
            new ButtonBorder(
                Color.white, //shadow
                Color.white, //darkShadow
                new Color(128,128,128), //highlight
                new Color(128,128,128) //lightHighlight
            ),
            BorderFactory.createEmptyBorder(1,1,1,1)
        );
        
        // Create the smiley new game button
        this.butNewGame = new JLabel(new ImageIcon(RES_SMILEY_NORMAL));
        this.butNewGame.setBorder(this.borderDepressed);
        this.butNewGame.addMouseListener(new MouseListener()
        {
            boolean mouseOverNewGameBut = false;
            @Override
            public void mousePressed(MouseEvent e)
            {
                if (SwingUtilities.isLeftMouseButton(e))
                {
                    butNewGame.setBorder(borderImpressed);
                }
            }
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (SwingUtilities.isLeftMouseButton(e))
                {
                    butNewGame.setBorder(borderDepressed);
                    if (mouseOverNewGameBut == true)
                    {
                        newGame();
                    }
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {mouseOverNewGameBut=false;}
            @Override
            public void mouseEntered(MouseEvent e) {mouseOverNewGameBut=true;}
            @Override
            public void mouseClicked(MouseEvent e) {}
        });
        JPanel butNewGamePanel = new JPanel();
        butNewGamePanel.setBackground(new Color(192,192,192));
        butNewGamePanel.setLayout(new FlowLayout());
        butNewGamePanel.add(this.butNewGame);
        
        // Create the top panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(192,192,192));
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(8,8,0,8),
            new ButtonBorder(
                Color.white, //shadow
                Color.white, //darkShadow
                new Color(128,128,128), //highlight
                new Color(128,128,128) //lightHighlight
            ) 
        ));
        topPanel.setLayout(new BorderLayout());
        topPanel.add(this.timerDisplay, "East");
        topPanel.add(this.minesLeftDisplay, "West");
        topPanel.add(butNewGamePanel, "Center");
        
        // Put all the parts together
        this.setLayout(new BorderLayout());
        this.add(topPanel, "North");
        this.add(this.mineGrid);
        
        // Get and set the difficulty from settings file
        int diffic = BEGINNER;
        try
        {
            diffic = this.getPreviousDifficulty(RES_SETTINGS);
            if (diffic == -1)
                diffic = BEGINNER;
        } 
        catch (Exception e)
        {
            diffic = BEGINNER;
        }
        this.setDifficulty(diffic);
        
        // Create the menu bar (must be done after setting difficulty so radio buttons set properly)
        this.setJMenuBar(new ui.MenuBar(this));
        
        // Resize all the components correctly
        this.pack();
        
        // Position the game in the center of the screen
        Rectangle screenSize = this.getGraphicsConfiguration().getBounds();
        this.setLocation(screenSize.x + screenSize.width/2  - this.getSize().width/2,
                         screenSize.y + screenSize.height/2 - this.getSize().height/2);
        
        this.setVisible(true);
    }
    
    /**
     * Sets the icon for the smiley new game button
     * 
     * @param iconName the path of the icon
     */
    public void setSmileyIcon(final String iconName)
    {
        this.butNewGame.setIcon(new ImageIcon(iconName));
    }
    
    /**
     * Gets the previous difficulty from a given file
     * 
     * @param filename the path to the file that contains the previous difficulty
     * @return the previous difficulty of the Minesweeper game, -1 if no prev difficulty
     * @throws FileNotFoundException if the settings file was not found
     * @throws IOException if there was an error reading the settings file
     */
    private int getPreviousDifficulty(final String filename) throws FileNotFoundException, IOException
    {
        DataInputStream in = new DataInputStream(new FileInputStream(filename));
        
        int _difficulty = -1;
        
        boolean eof = false;
        while ( ! eof)
        {
            try
            {
                _difficulty = in.readInt();
            }
            catch (EOFException eofEx) {eof = true;}          
        }
        
        in.close();
        
        return _difficulty;
    }
    
    /**
     * Writes the current difficulty to a given file
     * 
     * @param filename the path to the file to write the difficulty
     * @throws FileNotFoundException if the file to write to does not exist
     * @throws IOException if there was an error writing to file
     */
    private void writeDifficultyToFile(final String filename) throws FileNotFoundException, IOException
    {
        if ( ! (CUSTOM == this.difficulty)) // won't write custom games to file
        {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(filename));

            out.writeInt(this.difficulty);

            out.close();
        }
    }
    
    /**
     * Initiates a new, clean Minesweeper game
     */
    public void newGame()
    {
        this.gameOver = false;
        this.timerDisplay.resetTimer();
        this.setSmileyIcon(RES_SMILEY_NORMAL);
        
        // Set the mine grid
        if (difficulty == BEGINNER)
        {
            this.minesLeftDisplay.setNumMinesLeft(10);
            this.mineGrid.setGrid(9, 9, 10);
        }
        else if (difficulty == INTERMEDIATE)
        {
            this.minesLeftDisplay.setNumMinesLeft(40);
            this.mineGrid.setGrid(16, 16, 40);
        }
        else if (difficulty == EXPERT)
        {
            this.minesLeftDisplay.setNumMinesLeft(99);
            this.mineGrid.setGrid(30, 16, 99);
        }
        else if (difficulty == CUSTOM)
        {
            this.minesLeftDisplay.setNumMinesLeft(this.customNumMines);
            this.mineGrid.setGrid(this.customWidth, this.customHeight, this.customNumMines);
        }
    }
    
    /**
     * Sets the difficulty of the Minesweeper game
     * 
     * @param _difficulty the difficulty to set, Beginner, Intermediate, Expert or Custom
     */
    public void setDifficulty(final int _difficulty)
    {
        this.difficulty = _difficulty;
        this.newGame();
        this.pack(); //resize the window to fit the components
        try
        {
            this.writeDifficultyToFile(RES_SETTINGS);
        } 
        catch (Exception e) {} //do nothing if the save fails
    }
    
    /**
     * Sets the difficulty of a custom Minesweeper game
     * 
     * @param _difficulty the difficulty, usually Custom
     * @param width the width of the custom mineGrid
     * @param height the height of the custom mineGrid
     * @param numMines the number of mines on the custom mineGrid
     */
    public void setDifficulty(final int _difficulty, final int width,  final int height, final int numMines)
    {
        this.customWidth = width;
        this.customHeight = height;
        this.customNumMines = numMines;
        this.setDifficulty(_difficulty);
    }
    
    /**
     * Runs when the player has lost the Minesweeper game
     */
    public void lostGame()
    {
        this.gameOver = true;
        this.mineGrid.showMines();
        this.timerDisplay.stopTimer();
        this.setSmileyIcon(RES_SMILEY_DEAD);
    }
    
    /**
     * Runs when the player has won the Minesweeper game
     */
    public void wonGame()
    {
        this.gameOver = true;
        this.mineGrid.showMines();
        this.timerDisplay.stopTimer();
        this.setSmileyIcon(RES_SMILEY_COOL);
        
        // Handles the best times
        if ( ! (difficulty == CUSTOM))
        {
            if (this.bestTimes.isNewBestTime(this.timerDisplay.getNumSeconds(), difficulty))
            {
                String[] diffNames = {"Beginner", "Intermediate", "Expert"};
                
                // Alert the user he/she has a new best time
                String playerName = JOptionPane.showInputDialog(
                    this,
                    "You have the fastest time for "+diffNames[difficulty]+" level.\nPlease enter your name:",
                    "New best time!",
                    JOptionPane.QUESTION_MESSAGE
                );
                
                // Clip player names that are too long
                if (playerName.length() > 30)
                {
                    playerName = playerName.substring(0, 30);
                }

                this.bestTimes.addTime(playerName, this.timerDisplay.getNumSeconds(), difficulty);

                this.bestTimes.displayBestTimes();
            }
        }
    }
}
