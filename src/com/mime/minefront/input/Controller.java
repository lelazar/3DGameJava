package com.mime.minefront.input;

import com.mime.minefront.Display;

public class
Controller
{
    public double x, y, z, rotation, xa, za, rotationa;
    public static boolean turnLeft = false;
    public static boolean turnRight = false;
    public static boolean walk = false;
    public static boolean crouchWalk = false;
    public static boolean runWalk = false;

    public void
    tick(boolean forward, boolean back, boolean left, boolean right, boolean jump, boolean crouch, boolean run)
    {
        double rotationSpeed = 0.002 * Display.MouseSpeed;
        double walkSpeed = 0.8;
        double jumpHeight = 1.3;
        double crouchHeight = 0.7;
        double xMove = 0;
        double zMove = 0;

        // Basic control movements
        if(forward) {
            zMove++;  // If we request to move forward (the W key), we add one to z sort of every time it is pressed
            walk = true;
        }

        if(back) {
            zMove--;
            walk = true;
        }

        if(left) {
            xMove--;
            walk = true;
        }

        if(right) {
            xMove++;
            walk = true;
        }

        if(turnLeft) {
            rotationa -= rotationSpeed;
        }

        if(turnRight) {
            rotationa += rotationSpeed;
        }

        if(jump) {
            y+= jumpHeight;
            run = false;  // We cannot run while jumping
        }

        if(crouch) {
            y -= crouchHeight;
            run = false;  // We cannot run while crouching
            crouchWalk = true;
            walkSpeed = 0.3;
        }

        if(!crouch) {
            crouchWalk = false;
        }

        if(run) {
            walkSpeed = 1.6;
            walk = true;
            runWalk = true;
        }

        if(!run) {
            runWalk = false;
        }

        if(!forward && !back && !left && !right) {
            walk = false;
        }

        xa += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkSpeed;
        za += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkSpeed;

        x += xa;
        y *= 0.9;  // Maximum height
        z += za;
        xa *= 0.1;
        za *= 0.1;
        rotation += rotationa;
        rotationa *= 0.5;
    }
}














