/*
* An applet is a Java program that can run in a Web Browser.
 */

package com.mime.minefront;

import java.applet.Applet;
import java.awt.*;

public class
MinefrontApplet extends Applet
{
    private Display display = new Display();

    public void
    init()
    {
        setLayout(new BorderLayout());  // Setting a layout to our frame
        add(display);  // And adding it to the display
    }

    public void
    start()
    {
        display.start();
    }

    public void
    stop()
    {
        display.stop();
    }
}









