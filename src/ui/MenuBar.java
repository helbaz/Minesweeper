//********************************************
// The top menu bar for the game
// Created by Michael Seymour
// Created on 18 September 2011
//********************************************

package ui;

import javax.swing.*;
import java.awt.event.*;
import impl.Minesweeper;

/**
 * The top menu bar for the game
 */
public class MenuBar extends JMenuBar implements ActionListener
{
    /**
     * The main frame for the Minesweeper game
     */
    private Minesweeper main;
    
    /**
     * Constructor, creates a new menu bar
     * 
     * @param _main the main Minesweeper object
     */
    public MenuBar(final Minesweeper _main)
    {
        this.main = _main;
        
        JMenu gameMenu = new JMenu("Game");
        this.add(gameMenu);
        
        JMenuItem newGameItem = new JMenuItem("New Game");
        gameMenu.add(newGameItem);
        newGameItem.addActionListener(this);
        
        gameMenu.addSeparator();
        
        JRadioButtonMenuItem beginnerItem = new JRadioButtonMenuItem("Beginner", (this.main.getDifficulty()==this.main.BEGINNER) ? true : false);
        JRadioButtonMenuItem intermediateItem = new JRadioButtonMenuItem("Intermediate", (this.main.getDifficulty()==this.main.INTERMEDIATE) ? true : false);
        JRadioButtonMenuItem expertItem = new JRadioButtonMenuItem("Expert", (this.main.getDifficulty()==this.main.EXPERT) ? true : false);
        JRadioButtonMenuItem customItem = new JRadioButtonMenuItem("Custom...", false);
        gameMenu.add(beginnerItem);
        gameMenu.add(intermediateItem);
        gameMenu.add(expertItem);
        gameMenu.add(customItem);
        ButtonGroup difficultyBGroup = new ButtonGroup();
        difficultyBGroup.add(beginnerItem);
        difficultyBGroup.add(intermediateItem);
        difficultyBGroup.add(expertItem);
        difficultyBGroup.add(customItem);
        beginnerItem.addActionListener(this);
        intermediateItem.addActionListener(this);
        expertItem.addActionListener(this);
        customItem.addActionListener(this);
        customItem.setActionCommand("Custom");
        
        gameMenu.addSeparator();

        JMenuItem bestTimesItem = new JMenuItem("Best Times...");
        gameMenu.add(bestTimesItem);
        bestTimesItem.addActionListener(this);
        bestTimesItem.setActionCommand("Best Times");
                
        gameMenu.addSeparator();
        
        JMenuItem exitItem = new JMenuItem("Exit");
        gameMenu.add(exitItem);
        exitItem.addActionListener(this);
        
        // ---------------
        
        JMenu helpMenu = new JMenu("Help");
        this.add(helpMenu);

        JMenuItem viewHelpItem = new JMenuItem("View Help");
        helpMenu.add(viewHelpItem);
        viewHelpItem.addActionListener(this);

        helpMenu.addSeparator();

        JMenuItem aboutItem = new JMenuItem("About Minesweeper");
        helpMenu.add(aboutItem);
        aboutItem.addActionListener(this);
        aboutItem.setActionCommand("About");
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();
        
        if ("Beginner".equals(cmd))
        {
            this.main.setDifficulty(this.main.BEGINNER);
        }
        else if ("Intermediate".equals(cmd))
        {
            this.main.setDifficulty(this.main.INTERMEDIATE);
        }
        else if ("Expert".equals(cmd))
        {
            this.main.setDifficulty(this.main.EXPERT);
        }
        else if ("Custom".equals(cmd))
        {
            CustomGameDialog customDialog = new CustomGameDialog(this.main);
            this.main.setDifficulty(
                this.main.CUSTOM,
                customDialog.getWidthVal(), 
                customDialog.getHeightVal(), 
                customDialog.getNumMinesVal()
            );
        }
        else if ("New Game".equals(cmd))
        {
            this.main.newGame();
        }
        else if ("Best Times".equals(cmd))
        {
            this.main.getBestTimesObj().displayBestTimes();
        }
        else if ("Exit".equals(cmd))
        {
            System.exit(0);
        }
        else if ("View Help".equals(cmd))
        {
            JOptionPane.showMessageDialog(
                null,
                "The goal of Minesweeper is to uncover all the tiles that\n" +
                "do not contain mines. Clicking on a tile that does not\n" +
                "contain a mine will instead contain a number from 1 to 8,\n" +
                "indicating the number of mines adjacent to the uncovered\n" +
                "tile. If you click on a mine, you lose the game. You may\n" +
                "flag a tile you suspect to be a mine by right-clicking on\n" +
                "the tile. You can middle-click uncovered tiles to speed up\n" +
                "your game play. Good luck!\n",
                "Help", 
                JOptionPane.DEFAULT_OPTION
            );
        }
        else if ("About".equals(cmd))
        {
            JOptionPane.showMessageDialog(
                null,
                "A tribute to the original Minesweeper\n" +
                "Version: 1.0\n" +
                "Author: Michael Seymour\n" +
                "Year: 2011\n",
                "About Minesweeper", 
                JOptionPane.DEFAULT_OPTION
            );
        }
    }
}
