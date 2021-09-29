package com.mime.minefront.graphics;

import com.mime.minefront.Game;
import com.mime.minefront.input.Controller;

import java.util.Random;

public class
Render3D extends Render
{
    public double[] zBuffer;
    private double renderDistance = 5000;

    public
    Render3D(int width, int height)
    {
        super(width, height);
        zBuffer = new double[width * height];
    }

    public void
    floor(Game game)
    {
        double floorPosition = 8;
        double ceilingPosition = 32;
        double forward = game.controls.z;
        double right = game.controls.x;
        // double up = Math.sin(game.time / 10.0) * 2;  // We are using sine, because we want to jump up and then get down
        double up = game.controls.y;
        double walking = Math.sin(game.time / 6.0) * 0.5;  // Animating our "body" with imitating the movement while walking
        if(Controller.crouchWalk) {
            walking = Math.sin(game.time / 6.0) * 0.25;  // Making our body animation a bit slower while crouching
        }
        if(Controller.runWalk) {
            walking = Math.sin(game.time / 6.0) * 0.8;  // Making our body animation a bit faster while running
        }

        double rotation = game.controls.rotation;  // We do the animation here
        // Making a circle with the below parameters with using rotation
        double cosine = Math.cos(rotation);
        double sine = Math.sin(rotation);

        for(int y=0; y<height; y++)
        {
            double ceiling = (y - height / 2.0) / height;
            double z = (floorPosition + up) / ceiling;
            if(Controller.walk) z = (floorPosition + up + walking) / ceiling;

            if(ceiling < 0) {
                z = (ceilingPosition - up) / -ceiling;
                if(Controller.walk) z = (ceilingPosition - up - walking) / -ceiling;
            }

            for(int x=0; x<width; x++)
            {
                double depth = (x - width / 2.0) / height;
                depth *= z;
                double xx = depth * cosine + z * sine;  // Bitwise AND operator -> Read the explanation of Bitwise operation on Wikipedia!
                double yy = z * cosine - depth * sine;
                int xPix = (int) (xx + right);
                int yPix = (int) (yy + forward);
                zBuffer[x + y * width] = z;
                // pixels[x+y*width] = ((xPix & 15) * 16) | ((yPix & 15) * 16) << 8;
                pixels[x+y*width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) * 32];

                if(z > 500) pixels[x + y * width] = 0;
            }
        }
    }

    public void
    walls()
    {
        Random random = new Random(100);
        // Rendering a wall made by pixels (and adding texture on it)
        for(int i=0; i<20000; i++)
        {
            double xx = random.nextDouble();  // Horizontal
            double yy = random.nextDouble();  // Vertical
            double zz = 1.5;  // Depth

            int xPixel = (int) (xx / zz * height / 2 + width / 2);
            int yPixel = (int) (yy / zz * height / 2 + height / 2);
            // Want not to render things off the screen
            if(xPixel >= 0 && yPixel >= 0 && xPixel < width && yPixel < height)
            {
                pixels[xPixel + yPixel * width] = 0xffffff;
            }
        }
        for(int i=0; i<20000; i++)
        {
            double xx = random.nextDouble() - 1;  // Horizontal
            double yy = random.nextDouble();  // Vertical
            double zz = 1.5;  // Depth

            int xPixel = (int) (xx / zz * height / 2 + width / 2);
            int yPixel = (int) (yy / zz * height / 2 + height / 2);
            // Want not to render things off the screen
            if(xPixel >= 0 && yPixel >= 0 && xPixel < width && yPixel < height)
            {
                pixels[xPixel + yPixel * width] = 0xffffff;
            }
        }
        for(int i=0; i<20000; i++)
        {
            double xx = random.nextDouble() - 1;  // Horizontal
            double yy = random.nextDouble() - 1;  // Vertical
            double zz = 1.5;  // Depth

            int xPixel = (int) (xx / zz * height / 2 + width / 2);
            int yPixel = (int) (yy / zz * height / 2 + height / 2);
            // Want not to render things off the screen
            if(xPixel >= 0 && yPixel >= 0 && xPixel < width && yPixel < height)
            {
                pixels[xPixel + yPixel * width] = 0xffffff;
            }
        }
        for(int i=0; i<20000; i++)
        {
            double xx = random.nextDouble();  // Horizontal
            double yy = random.nextDouble() - 1;  // Vertical
            double zz = 1.5;  // Depth

            int xPixel = (int) (xx / zz * height / 2 + width / 2);
            int yPixel = (int) (yy / zz * height / 2 + height / 2);
            // Want not to render things off the screen
            if(xPixel >= 0 && yPixel >= 0 && xPixel < width && yPixel < height)
            {
                pixels[xPixel + yPixel * width] = 0xffffff;
            }
        }
    }

    public void
    renderDistanceLimiter()
    {
        for(int i=0; i<width*height; i++)
        {
            int colour = pixels[i];
            int brightness = (int) (renderDistance / zBuffer[i]);  // Brightness level is between 0 - 255!

            if(brightness < 0) brightness = 0;  // The minimum value of brightness must be 0
            if(brightness > 255) brightness = 255;  // The maximum value of brightness must be 255

            // This is how it can be programmed:
            int r = (colour >> 16) & 0xff;
            int g = (colour >> 8) & 0xff;
            int b = (colour) & 0xff;

            r = r * brightness / 255;
            g = g * brightness / 255;
            b = b * brightness / 255;

            pixels[i] = r << 16 | g << 8 | b;
        }
    }
}

/*
sx = x / z tan(fov / 2)
sy = y / z tan(fov / 2)
Where sx, sy are normalized screen cords and x, y, z are 3d space coords
You can exclude the tan to get a fov of 90 degrees

Then since already know sx, sy and y(the floor/ceiling distance/height) you loop through the y coordinate of the screen and for each row solve for z:
z = 1 / sy * y
Then you have a z distance for every horizontal row of pixels
Next, for every horizontal row you loop through all the pixels based on the distance of the row from the camera,
x = sx * z

Now you have top down 2d cords x and z which you can directly translate to texture coordinates
 */

/*
        pixels[11 + 10 * width] = 0xffffff;
        pixels[12 + 10 * width] = 0xffffff;
        pixels[13 + 10 * width] = 0xffffff;
        pixels[14 + 10 * width] = 0xffffff;
        pixels[15 + 10 * width] = 0xffffff;
        pixels[16 + 10 * width] = 0xffffff;
 */







