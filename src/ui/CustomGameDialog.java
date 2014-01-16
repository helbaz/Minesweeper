//********************************************
// The dialog used when creating a custom game
// Created by Michael Seymour
// Created on 18 September 2011
//********************************************

package ui;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import impl.Minesweeper;

/**
 * The dialog used when creating a custom game
 */
public class CustomGameDialog extends JDialog implements ActionListener, WindowListener
{
    private int widthVal;
    /**
     * Returns the width setting in the dialog
     * 
     * @return the width setting in the dialog
     */
    public int getWidthVal() {return this.widthVal;}
    
    private int heightVal;
    /**
     * Returns the height setting in the dialog
     * 
     * @return the height setting in the dialog
     */
    public int getHeightVal() {return this.heightVal;}
    
    private int numMinesVal;
    /**
     * Returns the number of mines setting in the dialog
     * 
     * @return the number of mines setting in the dialog
     */
    public int getNumMinesVal() {return this.numMinesVal;}
    
    /**
     * The fields used to enter the settings in the dialog
     */
    private JTextField widthField = new JTextField(5);
    private JTextField heightField = new JTextField(5);
    private JTextField numMinesField = new JTextField(5);
    
    /**
     * Constructor, creates a new custom game dialog
     * 
     * @param main the main Minesweeper object
     */
    public CustomGameDialog(final Minesweeper main)
    {
        // Set the parent frame and dialog title
        super(main, "Custom", true);
        
        // Get the current game settings
        this.widthVal = main.getMineGrid().getGridWidth();
        this.heightVal = main.getMineGrid().getGridHeight();
        this.numMinesVal = main.getMineGrid().getNumMines();

        // Set the default dialog field values
        this.widthField.setText(""+this.widthVal);
        this.heightField.setText(""+this.heightVal);
        this.numMinesField.setText(""+this.numMinesVal);
        
        // Start building the dialog
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints(
            2, // The initial gridx value.
            4, // The initial gridy value.
            1, // The initial gridwidth value.
            1, // The initial gridheight value.
            0.0, // The initial weightx value.
            1.0, // The initial weighty value.
            GridBagConstraints.CENTER, // The initial anchor value.
            GridBagConstraints.NONE, // The initial fill value.
            new Insets(5, 5, 5, 5), // The initial insets value.
            0, // The initial ipadx value.
            0 // The initial ipady value.
        );
        
        c.gridx = 1;
        c.gridy = 3;

        JButton butOK = new JButton("OK");
        this.add(butOK, c);
        butOK.addActionListener(this);

        c.gridx = 0;
        c.anchor = GridBagConstraints.EAST;

        c.gridy = 0;
        this.add(new JLabel("Width (9-30):"),c);
        c.gridy = 1;
        this.add(new JLabel("Height (9-24):"),c);
        c.gridy = 2;
        this.add(new JLabel("Mines (10-668):"),c);

        c.gridx = 1;
        c.gridy = GridBagConstraints.RELATIVE;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;

        c.gridy = 0;
        this.add(widthField, c);
        c.gridy = 1;		
        this.add(heightField, c);
        c.gridy = 2;		
        this.add(numMinesField, c);
        
        this.pack();
        
        Rectangle screenSize = this.getGraphicsConfiguration().getBounds();
        setLocation(screenSize.x + screenSize.width/2  - this.getSize().width/2,
                    screenSize.y + screenSize.height/2 - this.getSize().height/2 );
        
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (setValues())
        {
            this.dispose();
        }
    }
    
    /**
     * Sets the values of this objects variables from the input supplied in the dialog
     * 
     * @return true if the values were set correctly, false otherwise
     */
    public boolean setValues()
    {
        try
        {
            this.widthVal = Integer.decode(widthField.getText()).intValue();
            if (this.widthVal < 9)
                this.widthVal = 9;
            else if (this.widthVal > 30)
                this.widthVal = 30;
            
            this.heightVal = Integer.decode(heightField.getText()).intValue();
            if (this.heightVal < 9)
                this.heightVal = 9;
            else if (this.heightVal > 24)
                this.heightVal = 24;
            
            this.numMinesVal = Integer.decode(numMinesField.getText()).intValue();
            if (this.numMinesVal < 10)
                this.numMinesVal = 10;
            else if (this.numMinesVal > 668)
                this.numMinesVal = 668;
        }
        catch(NumberFormatException e)
        {
            return false;
        }

        return true;
    }
    
    @Override
    public void windowClosing(WindowEvent evt)
    {
        if (setValues())
        {
            dispose();
        }
    }

    @Override
    public void windowOpened(WindowEvent evt) {}
    @Override
    public void windowClosed(WindowEvent evt) {}		
    @Override
    public void windowIconified(WindowEvent evt) {}	
    @Override
    public void windowDeiconified(WindowEvent evt) {}		
    @Override
    public void windowActivated(WindowEvent evt) {}		
    @Override
    public void windowDeactivated(WindowEvent evt) {}
}
