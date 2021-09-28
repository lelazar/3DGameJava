package com.mime.minefront;

import com.mime.minefront.input.Controller;

import java.awt.event.KeyEvent;

public class
Game
{
    public int time;
    public Controller controls;

    public Game()
    {
        controls = new Controller();
    }

    public void
    tick(boolean[] key)
    {
        time++;
        // Initializing the keys as booleans, because a key can be pressed or released (if the boolean equals true, the key is pressed)
        boolean forward = key[KeyEvent.VK_W];
        boolean back = key[KeyEvent.VK_S];
        boolean left = key[KeyEvent.VK_A];
        boolean right = key[KeyEvent.VK_D];
        boolean jump = key[KeyEvent.VK_SPACE];
        boolean crouch = key[KeyEvent.VK_CONTROL];
        boolean run = key[KeyEvent.VK_SHIFT];
        // boolean turnLeft = key[KeyEvent.VK_LEFT];
        // boolean turnRight = key[KeyEvent.VK_RIGHT];

        controls.tick(forward, back, left, right, jump, crouch, run);
    }
}






