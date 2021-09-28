package com.mime.minefront.graphics;

import com.mime.minefront.Game;

import java.util.Random;

public class
Screen extends Render
{
    private Render test;
    private Render3D render;

    public
    Screen(int width, int height)
    {
        super(width, height);
        Random random = new Random();
        render = new Render3D(width, height);
        test = new Render(256, 256);  // Inherited from the Render class' constructor
        for(int i=0; i<256*256; i++)
        {
            test.pixels[i] = random.nextInt();
        }
    }

    public void
    render(Game game)
    {
        for(int i=0; i<width*height; i++)
        {
            pixels[i] = 0;  // Setting pixels to 0 in every frame (removing the background of the animated rectangle)
        }
        render.floor(game);
        render.renderDistanceLimiter();
        draw(render, 0, 0);
    }
}
/*
        for (int i=0; i<50; i++) {  // For example if we want to loop it until 1000, we can add i * 5 (200 * 5) to the Math.sin and Math.cos and we can have better performance instead if we are looping it until i<1000
            // int anim = (int) (Math.sin((System.currentTimeMillis() + i * 8) % 2000.0 / 2000 * Math.PI * 2) * 200);
            // int anim2 = (int) (Math.cos((System.currentTimeMillis() + i * 8) % 2000.0 / 2000 * Math.PI * 2) * 200);
            // int anim0 = (int) (Math.sin(System.currentTimeMillis() % 1000.0 / 1000 * Math.PI * 2) * 1000);
            int anim = (int) (Math.sin((game.time + i * 2) % 1000.0 / 100) * 100);
            int anim2 = (int) (Math.cos((game.time + i * 2) % 1000.0 / 100) * 100);

            // draw(test, (width - 256) / 2 + anim, (height - 256) / 2 + anim2);  // Inherited from the Render class' draw method and positions it to the center of the screen
        }
 */














