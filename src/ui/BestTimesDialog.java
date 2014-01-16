//********************************************
// A dialog displaying the best times
// Created by Michael Seymour
// Created on 18 September 2011
//********************************************

package ui;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import data.SingleTime;
import impl.BestTimes;

/**
 * A dialog displaying the best times
 */
public class BestTimesDialog extends JDialog implements ActionListener
{
    /**
     * The parent, best times handler
     */
    private BestTimes bestTimesObj;
    
    /**
     * Constructor, creates a new best times dialog
     * 
     * @param _bestTimes the main BestTimes object
     */
    public BestTimesDialog(final BestTimes _bestTimes)
    {        
        this.bestTimesObj = _bestTimes;
        this.setUndecorated(true); //removes the window frame
                
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints(
            2, //gridx
            4, //gridy
            1, //gridwidth
            1, //gridheight
            0.0, //weightx
            1.0, //weighty
            GridBagConstraints.CENTER, //anchor
            GridBagConstraints.NONE, //fill
            new Insets(10, 10, 10, 10), //insets
            0, //ipadx
            0 //ipady
        );
        
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        
        JLabel header = new JLabel("Fastest Mine Sweepers");
        header.setHorizontalAlignment(JLabel.CENTER);
        this.add(header,c);
        
        c.gridx = 0;
        c.gridy = 4;
        
        JPanel butNewGamePanel = new JPanel();
        butNewGamePanel.setLayout(new FlowLayout());

        JButton butReset = new JButton("Reset Scores");
        butReset.addActionListener(this);
        butNewGamePanel.add(butReset);
        
        JButton butOK = new JButton("OK");
        butOK.addActionListener(this);
        butNewGamePanel.add(butOK);
        
        this.add(butNewGamePanel, c);
        
        c.gridwidth = 1;
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(2, 10, 2, 10);

        c.gridy = 1;
        this.add(new JLabel("Beginner:"), c);
        c.gridy = 2;
        this.add(new JLabel("Intermediate:"), c);
        c.gridy = 3;
        this.add(new JLabel("Expert:"), c);
        
        // Iterate over the best times and add each one
        for (int i=0; i<this.bestTimesObj.getBestTimes().size(); i++)
        {
            c.gridx = 1;
            c.gridy = GridBagConstraints.RELATIVE;
            c.weightx = 1.0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.CENTER;

            c.gridy = i+1;
            this.add(new JLabel(((SingleTime)this.bestTimesObj.getBestTimes().get(i)).getTime()+" seconds"), c);
            
            c.gridx = 2;
        
            c.gridy = i+1;
            this.add(new JLabel(((SingleTime)this.bestTimesObj.getBestTimes().get(i)).getName()), c);
        }
        
        this.pack();
        
        // Move the best times window to the center of the game window
        this.setLocationRelativeTo(bestTimesObj.getMainFrame());
        
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();
        
        if ("OK".equals(cmd))
        {
            dispose();
        }
        else if ("Reset Scores".equals(cmd))
        {
            this.bestTimesObj.resetBestTimes();
            dispose();
        }
    } 
}
