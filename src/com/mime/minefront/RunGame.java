package com.mime.minefront;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class
RunGame
{
    public RunGame()
    {
        BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);  // Mouse pointer
        Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "blank");
        Display game = new Display();
        JFrame frame = new JFrame();
        frame.add(game);
        frame.setSize(Display.getGameWidth(), Display.getGameHeight());
        // frame.pack();
        frame.getContentPane().setCursor(blank);  // We are removing the cursor pointer of the mouse from the screen
        frame.setTitle(Display.TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Otherwise, it will not terminate itself
        frame.setLocationRelativeTo(null);  // Position the window to the center of our screen
        frame.setResizable(false);
        frame.setVisible(true);

        game.start();
    }
}
