package com.mime.minefront.input;

import java.awt.event.*;

public class
InputHandler implements KeyListener, FocusListener, MouseListener, MouseMotionListener
{
    public boolean[] key = new boolean[68836];
    public static int MouseX;
    public static int MouseY;
    public static int MouseDX;  // D = Drag
    public static int MouseDY;  // D = Drag
    public static int MousePX;  // P = Pressed
    public static int MousePY;  // P = Pressed
    public static int MouseButton;
    public static boolean dragged = false;

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {  // Need to set this to not doing the movements while the window is not focused
        for(int i=0; i< key.length; i++)
        {
            key[i] = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode > 0 && keyCode < key.length)
        {
            key[keyCode] = true;  // If we press a key on the keyboard, we want to make sure that the computer understands that we pressed a key
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if(keyCode > 0 && keyCode < key.length)
        {
            key[keyCode] = false;  // If key is released, we are setting keyPressed to false
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        MouseButton = e.getButton();
        MousePX = e.getX();
        MousePY = e.getY();
        //dragged = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragged = false;
        MouseButton = 0;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        dragged = true;
        MouseDX = e.getX();
        MouseDY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Assigning the two public static variables to the current vertical and horizontal position of the Mouse
        MouseX = e.getX();  // getX() is relative to the actual frame window!
        MouseY = e.getY();
    }
}





