/*******************************************************************************************************************************************************************
 * In this program, we are only using the Java's built-in libraries. Not going to make some high-end graphics and we are not going to use
 * the full potential of our GPU with OpenGL or anything like that. Only using built-in libraries and functions/methods and doing everything from scratch.
 * This is designed to show how to make a game without potentially using the graphics card, only learning the Java language.
 * Using the 3D Game Programming in Java series by the YouTube channel 'The Cherno' (https://www.youtube.com/c/TheChernoProject/about)
 * Most of the techniques are commented with line comments, but reading the Java Documentation about the built-in classes and functions are highly recommended!
 * Java Platform SE 6 API Specification: https://docs.oracle.com/javase/6/docs/api/
 * Copyright by Levente Lázár, 2021
 ********************************************************************************************************************************************************************/

/*******************************************************************************************************************************************************************
 * Techniques we are using in this 3D FPS program:
 * Drawing pixels, BufferedImage, BufferedInt, BufferStrategy, Graphics;
 * Rendering, animating pixels and graphics;
 * FPS Counter, Alpha support, Bitwise operators;
 * Making square floors and ceilings, animating depth, animate walking in a 3D world;
 * Rotation, user input/controls, limiting the render distance, using the mouse to look around a 3D world;
 * Draw text to the screen, adding textures, adding crouch/walk/sprint/jump, making and rendering walls;
 * Manage and prevent crashes, clip correctly via the Cohen-Sutherland algorithm, detecting mouse speed;
 * Generating random walls, creating GUI (Graphical User Interface), creating a Java application, adding buttons;
 * Write to file and read from files, storing configuration data in XML files;
 * Let the user create custom resolutions with the help of labels (JLabel) and text fields (JTextFields);
 *******************************************************************************************************************************************************************/

package com.mime.minefront;

import com.mime.minefront.graphics.Screen;
import com.mime.minefront.gui.Launcher;
import com.mime.minefront.input.Controller;
import com.mime.minefront.input.InputHandler;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class
Display extends Canvas implements Runnable  // Canvas is a black rectangular area of the screen and Runnable should be implemented when instances are intended to be executed by a thread
{
    public static final long serialVersionUID = 1L;

    // public static final int WIDTH = 800;
    // public static final int HEIGHT = 600;
    public static int width = 800;
    public static int height = 600;
    public static final String TITLE = "Minefront Pre-Alpha 0.02";

    private Thread thread;  // Do multiple tasks simultaneously at the same time
    private boolean running = false;  // The game is not yet running at this moment
    private Screen screen;
    private Game game;
    private BufferedImage img;  // Describes an image with an accessible buffer of image data
    private int[] pixels;
    private InputHandler input;
    private int newX = 0;
    private int oldX = 0;
    private int fps;
    public static int selection = 0;  // Helps us to get the selection index of our resolution option in the GUI
    // Launcher launcher = new Launcher(0, this);

    public static int MouseSpeed;

    public
    Display()
    {
        Dimension size = new Dimension(getGameWidth(), getGameHeight());
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        screen = new Screen(getGameWidth(), getGameHeight());
        game = new Game();
        img = new BufferedImage(getGameWidth(), getGameHeight(), BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();  // Casting it to DataBufferInt

        input = new InputHandler();
        addKeyListener(input);
        addFocusListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);
    }

    public static int
    getGameWidth()
    {
        return width;
    }

    public static int
    getGameHeight()
    {
        return height;
    }

    public synchronized void  // synchronized helps with threads to run properly - a safety precaution when we are using threads
    start()
    {
        if(running) return;  // If the game is already running, we do not want to initialize it again (so we return)
        running = true;
        thread = new Thread(this, "game");
        thread.start();

        // TEST:
        // System.out.println("Working!");
    }

    public synchronized void  // synchronized helps with threads to run properly - a safety precaution when we are using threads
    stop()
    {
        if(!running) return;
        running = false;
        try
        {
            thread.join();  // Tries and waits for this thread to die
        } catch (Exception e) {
            e.printStackTrace();  // If it is not working, print a stack trace and exit
            System.exit(0);
        }

    }

    public void
    run()
    {
        // FPS counter to the console for testing purposes
        int frames = 0;
        double unprocessedSeconds = 0;
        long previousTime = System.nanoTime();  // Highly precise!
        double secondsPerTick = 1 / 60.0;
        int tickCount = 0;
        boolean ticked = false;
        requestFocus();  // When launched, it is activated immediately, we do not actually need to click on the screen to activate it

        while(running)
        {
            long currentTime = System.nanoTime();
            long passedTime = currentTime - previousTime;
            previousTime = currentTime;
            unprocessedSeconds += passedTime / 1_000_000_000.0;
            //launcher.updateFrame();

            while(unprocessedSeconds > secondsPerTick)
            {
                tick();
                unprocessedSeconds -= secondsPerTick;
                ticked = true;
                tickCount++;
                if(tickCount % 60 == 0)
                {
                    // System.out.println(frames + "fps");
                    fps = frames;
                    previousTime += 1000;
                    frames = 0;
                }
                if(ticked)
                {
                    //render();
                    //renderMenu();
                    frames++;
                }
                //render();
            }


            // Test for getting the mouse X and Y coordinates: System.out.println("X: " + InputHandler.MouseX + " Y: " + InputHandler.MouseY);

            // Creating and testing the mouse movement
            newX = InputHandler.MouseX;
            if(newX > oldX)
            {
                // System.out.println("Right!!!");
                Controller.turnRight = true;
            }
            if(newX < oldX)
            {
                // System.out.println("Left!!!");
                Controller.turnLeft = true;
            }
            if(newX == oldX)
            {
                // System.out.println("Still!!!");
                Controller.turnLeft = false;
                Controller.turnRight = false;
            }
            MouseSpeed = Math.abs(newX - oldX);  // Every time it updates, it figures out the amount and the difference that the mouse speed is changed by
            oldX = newX;
        }
    }

    private void
    tick()
    {
        game.tick(input.key);
    }

    private void
    render()  // Gets updated every time, because it is in the while loop above in the run()
    {
        BufferStrategy bs = this.getBufferStrategy();  // Organizing complex memory on a particular Canvas or Window
        // Want to start Buffer Strategy once
        if(bs == null)
        {
            createBufferStrategy(3);  // 3D Game
            return;
        }

        screen.render(game);

        for(int i=0; i<getGameWidth()*getGameHeight(); i++)
        {
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics(); // Graphics is an abstract base class for all graphics contexts that allow an application to draw onto components that are realized on various devices, as well as onto off-screen images
        g.drawImage(img, 0, 0, getGameWidth() + 10, getGameHeight() + 10, null);  // Draw as much of the specified image as has already been scaled to fit inside the specified rectangle
        g.setFont(new Font("Verdana", 1, 35));
        g.setColor(Color.yellow);
        g.drawString(fps + " FPS", 15, 45);  // Drawing the FPS counter to the window
        g.dispose();  // Dispose of this graphics context and releases any system resources that it is using
        bs.show();  // Makes the next available buffer visible by either copying the memory or changing the display pointer
    }

    public static void
    main(String[] args)
    {
        Display display = new Display();
        new Launcher(0, display);  // We do not need to create an object from this Launcher class, unless we want to use any of its methods or variables, etc.
    }
}
















