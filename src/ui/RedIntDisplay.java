//********************************************
// Base class for the displays with red numbers on a black background
// Created by Michael Seymour
// Created on 18 September 2011
//********************************************

package ui;

import javax.swing.plaf.basic.BasicBorders.ButtonBorder;
import javax.swing.border.MatteBorder;
import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * Base class for the displays with red numbers on a black background
 */
public class RedIntDisplay extends JLabel
{
    /**
     * Constructor, creates a new instance of RedDisplay
     */
    public RedIntDisplay()
    {
        this.setForeground(Color.RED);
        this.setOpaque(true);
        this.setBackground(Color.BLACK);
        Font customFont = new Font(this.getFont().getName(),this.getFont().getStyle(),18);
        this.setFont(customFont);
        this.setHorizontalAlignment(JLabel.RIGHT);
        this.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(
                7, //top
                7, //left
                7, //bottom
                7, //right
                new Color(192,192,192)
            ),
            new ButtonBorder(
                Color.white, //shadow
                Color.white, //darkShadow
                new Color(128,128,128), //highlight
                new Color(128,128,128) //lightHighlight
            )
        ));
        this.setPreferredSize(new Dimension(51, 1));
    }
    
    /**
     * Sets the number on the display
     * 
     * @param num the number to set
     */
    public void setDisplayNum(final int num)
    {
        this.setText(this.padInt(num, 3));
    }
    
    /**
     * Pads a number with 0's to its left
     * 
     * @param num the number to pad
     * @param digits the length of the final, padded string
     * @return the padded number as a string
     */
    private String padInt(final int num, final int digits)
    {
        int _digits = digits;
        
        if (num < 0)
        {
            _digits--; //this fix wont work for numbers less than or eq to -100
        }
        
        // Create variable length array of zeros
        char[] zeros = new char[_digits];
        Arrays.fill(zeros, '0');
        
        // Format number as String
        DecimalFormat df = new DecimalFormat(String.valueOf(zeros));
        
        return df.format(num);
    }
}
