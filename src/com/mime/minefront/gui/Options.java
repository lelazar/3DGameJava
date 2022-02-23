package com.mime.minefront.gui;

import com.mime.minefront.Display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class
Options extends Launcher
{
    private int width = 550;
    private int height = 450;
    private JButton OK;
    private JTextField twidth, theight;
    private JLabel lwidth, lheight;
    private Rectangle rOK, rresolution;
    private Choice resolution = new Choice();  // Presents a pop-up menu for choices
    int w=0, h=0;

    public Options()
    {
        super(1, new Display());  // When we hit 'Options', it is going to run whatever that is in the constructor of Launcher()
        // @Override
        frame.setTitle("Options - Minefront Launcher");
        setSize(new Dimension(width, height));
        frame.setLocationRelativeTo(null);
        drawButtons();
    }

    private void
    drawButtons()
    {
        OK = new JButton("OK");
        rOK = new Rectangle((width-100), (height-70), button_width - 10, button_height - 20);
        OK.setBounds(rOK);
        window.add(OK);

        rresolution = new Rectangle(50, 80, 80, 25);
        resolution.setBounds(rresolution);
        resolution.add("640, 480");
        resolution.add("800, 600");
        resolution.add("1024, 768");
        resolution.select(1);  // Default selected option (in our case, it will be "800, 600")
        window.add(resolution);

        lwidth = new JLabel("Width: ");
        lwidth.setBounds(30, 150, 120, 20);
        // lwidth.setFont(new Font("Verdana", 2, 50));
        window.add(lwidth);

        lheight = new JLabel("Height: ");
        lheight.setBounds(30, 180, 120, 20);
        window.add(lheight);

        twidth = new JTextField();
        twidth.setBounds(80, 150, 60, 20);
        window.add(twidth);

        theight = new JTextField();
        theight.setBounds(80, 180, 60, 20);
        window.add(theight);

        OK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new Launcher(0, new Display());
                config.saveConfiguration("width", parseWidth());
                config.saveConfiguration("height", parseHeight());
            }
        });
    }

    private void
    drop()
    {
        int selection = resolution.getSelectedIndex();  // We need this to get the selected index of our resolution option
        if(selection == 0)
        {
            w = 640;
            h = 480;
        }
        if(selection == 1 || selection == -1)  // -1 also to avoid NullPointerException
        {
            w = 800;
            h = 600;
        }
        if(selection == 2)
        {
            w = 1024;
            h = 768;
        }
    }

    private int
    parseWidth()
    {
        try {
            int w = Integer.parseInt(twidth.getText());
            return w;  // This w is created above, in the 'try', it is not the same with the below one!
        } catch (NumberFormatException e) {
            drop();
            return w;
        }
    }

    private int
    parseHeight()
    {
        try {
            int h = Integer.parseInt(theight.getText());
            return h;
        } catch (NumberFormatException e) {
            drop();
            return h;
        }
    }
}










