package com.mime.minefront.gui;

import com.mime.minefront.Configuration;
import com.mime.minefront.Display;
import com.mime.minefront.RunGame;
import com.mime.minefront.input.InputHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.Objects;

public class
Launcher extends Canvas implements Runnable
{
    protected JPanel window = new JPanel();  // protected means that this variable is visible in this class and in every class that extends from this one
    private JButton play, options, help, quit;
    private Rectangle rplay, roptions, rhelp, rquit;
    Configuration config = new Configuration();

    private int width = 800;
    private int height = 400;
    protected int button_width = 80;   // protected means that this variable is visible in this class and in every class that extends from this one
    protected int button_height = 40;  // protected means that this variable is visible in this class and in every class that extends from this one
    boolean running = false;
    Thread thread;
    JFrame frame = new JFrame();

    public Launcher(int id, Display display)
    {
        // Making our buttons look like Windows buttons
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        frame.setUndecorated(true);  // It will look like just like the Skyrim launcher
        frame.setTitle("Minefront Launcher");
        frame.setSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //getContentPane().add(window);
        frame.add(this);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        window.setLayout(null);

        if(id == 0) {
            drawButtons();
        }
        InputHandler input = new InputHandler();
        addKeyListener(input);
        addFocusListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);
        startMenu();
        display.start();
        frame.repaint();
    }

    public void
    updateFrame()
    {
        // We want to only move the frame if we detect that it is getting dragged
        if(InputHandler.dragged) {
            Point p = frame.getLocation();  // Need to put frame. before, because both Canvas and JFrame has a getLocation() function, and in this case, Canvas' will be chosen.
            frame.setLocation(p.x + InputHandler.MouseDX - InputHandler.MousePX, p.y + InputHandler.MouseDY - InputHandler.MousePY);
        }
    }

    public void
    startMenu()
    {
        running = true;
        thread = new Thread(this, "menu");
        thread.start();
    }

    public void
    stopMenu()
    {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        requestFocus();
        while(running)
        {
            renderMenu();
            updateFrame();
        }
    }

    private void
    renderMenu()  // Gets updated every time, because it is in the while loop above in the run()
    {
        BufferStrategy bs = getBufferStrategy();  // Organizing complex memory on a particular Canvas or Window
        // Want to start Buffer Strategy once
        if(bs == null)
        {
            createBufferStrategy(3);  // 3D Game
            return;
        }

        Graphics g = bs.getDrawGraphics(); // Graphics is an abstract base class for all graphics contexts that allow an application to draw onto components that are realized on various devices, as well as onto off-screen images
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 400);
        try {
            g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu-image.jpg"))), 0, 0, 800, 400, null);
            if(InputHandler.MouseX > 690 && InputHandler.MouseX < 690+80 && InputHandler.MouseY > 130 && InputHandler.MouseY < 130+30) {
                g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/play_on.png"))), 690, 130, 80, 30, null);
                g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/arrow.png"))), 690+70, 134, 22, 20, null);
                if(InputHandler.MouseButton == 1) {
                    System.out.println("Play!");
                }
            } else {
                g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/play_off.png"))), 690, 130, 80, 30, null);
            }
            if(InputHandler.MouseX > 641 && InputHandler.MouseX < 690+130 && InputHandler.MouseY > 170 && InputHandler.MouseY < 170+30) {
                g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/options_on.png"))), 641, 170, 130, 30, null);
                g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/arrow.png"))), 690+70, 174, 22, 20, null);
                if(InputHandler.MouseButton == 1) {
                    System.out.println("Options!");
                }
            } else {
                g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/options_off.png"))), 641, 170, 130, 30, null);
            }
            if(InputHandler.MouseX > 690 && InputHandler.MouseX < 690+80 && InputHandler.MouseY > 210 && InputHandler.MouseY < 210+30) {
                g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/help_on.png"))), 690, 210, 80, 30, null);
                g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/arrow.png"))), 690+70, 214, 22, 20, null);
                if(InputHandler.MouseButton == 1) {
                    System.out.println("Help!");
                }
            } else {
                g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/help_off.png"))), 690, 210, 80, 30, null);
            }
            if(InputHandler.MouseX > 690 && InputHandler.MouseX < 690+80 && InputHandler.MouseY > 250 && InputHandler.MouseY < 250+30) {
                g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/exit_on.png"))), 690, 250, 80, 30, null);
                g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/arrow.png"))), 690+70, 254, 22, 20, null);
                if(InputHandler.MouseButton == 1) {  // If our mouse is over the 'exit' button AND if we click -> close the program
                    System.exit(0);
                }
            } else {
                g.drawImage(ImageIO.read(Objects.requireNonNull(Launcher.class.getResource("/menu/exit_off.png"))), 690, 250, 80, 30, null);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        g.dispose();  // Dispose of this graphics context and releases any system resources that it is using
        bs.show();  // Makes the next available buffer visible by either copying the memory or changing the display pointer
    }

    private void
    drawButtons()
    {
        play = new JButton("Play!");
        rplay = new Rectangle((width/2)-(button_width/2), 90, button_width, button_height);
        play.setBounds(rplay);
        window.add(play);

        options = new JButton("Options");
        roptions = new Rectangle((width/2)-(button_width/2), 140, button_width, button_height);
        options.setBounds(roptions);
        window.add(options);

        help = new JButton("Help");
        rhelp = new Rectangle((width/2)-(button_width/2), 190, button_width, button_height);
        help.setBounds(rhelp);
        window.add(help);

        quit = new JButton("Quit");
        rquit = new Rectangle((width/2)-(button_width/2), 240, button_width, button_height);
        quit.setBounds(rquit);
        window.add(quit);

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                config.loadConfiguration("res/settings/config.xml");
                frame.dispose();  // When we hit 'Play!', the GUI Launcher will disappear
                new RunGame();
            }
        });

        options.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new Options();
            }
        });

        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Help!");
            }
        });

        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);  // 0 means a normal exit; 1 means an abnormal exit
            }
        });
    }
}















