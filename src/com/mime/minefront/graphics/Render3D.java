package com.mime.minefront.graphics;

import com.mime.minefront.Game;
import com.mime.minefront.input.Controller;
import com.mime.minefront.level.Block;
import com.mime.minefront.level.Level;

public class
Render3D extends Render
{
    public double[] zBuffer;
    public double[] zBufferWall;
    private double renderDistance = 5000;
    private double forward, right, cosine, sine, up, walking;

    public
    Render3D(int width, int height)
    {
        super(width, height);
        zBuffer = new double[width * height];
        zBufferWall = new double[width];
    }

    public void
    floor(Game game)
    {
        for(int x=0; x<width; x++)
        {
            zBufferWall[x] = 0;  // Make sure the walls behind other walls won't be rendered
        }

        double floorPosition = 8;
        double ceilingPosition = 8;
        forward = game.controls.z;
        right = game.controls.x;
        up = game.controls.y;
        walking = 0;
        double rotation = /*Math.sin(game.time / 40.0) * 0.5;*/ game.controls.rotation;  // We do the animation here
        // Making a circle with the below parameters with using rotation
        cosine = Math.cos(rotation);
        sine = Math.sin(rotation);

        for(int y=0; y<height; y++)
        {
            double ceiling = (y - height / 2.0) / height;
            double z = (floorPosition + up) / ceiling;

            if(Controller.walk) {
                walking = Math.sin(game.time / 6.0) * 0.5;  // Animating our "body" with imitating the movement while walking
                z = (floorPosition + up + walking) / ceiling;
            }
            if(Controller.crouchWalk && Controller.walk) {
                walking = Math.sin(game.time / 6.0) * 0.25;  // Making our body animation a bit slower while crouching
                z = (floorPosition + up + walking) / ceiling;
            }
            if(Controller.runWalk && Controller.walk) {
                walking = Math.sin(game.time / 6.0) * 0.8;  // Making our body animation a bit faster while running
                z = (floorPosition + up + walking) / ceiling;
            }

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
                pixels[x+y*width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) * 16];  // Apply texture to the floor (multiply it by 16, because our floor texture is 16 pixels wide

                if(z > 500) pixels[x + y * width] = 0;
            }
        }

        // Generating and rendering walls in a specific level
        Level level = game.level;
        int size = 50;  // Size of our level, where the walls will be generated
        // Render it for us
        for(int xBlock=-size; xBlock<=size; xBlock++)
        {
            for(int zBlock=-size; zBlock<=size; zBlock++)
            {
                // We need some blocks
                Block block = level.create(xBlock, zBlock);
                Block east = level.create(xBlock + 1, zBlock);
                Block south = level.create(xBlock, zBlock + 1);

                // Generating them
                if(block.solid)
                {
                    if(!east.solid)
                    {
                        renderWall(xBlock + 1, xBlock +1, zBlock, zBlock + 1, 0);
                    }
                    if(!south.solid)
                    {
                        renderWall(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 0);
                    }
                }
                else
                {
                    if(east.solid)
                    {
                        renderWall(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 0);
                    }
                    if(south.solid)
                    {
                        renderWall(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 0);
                    }
                }
            }
        }
        for(int xBlock=-size; xBlock<=size; xBlock++)
        {
            for(int zBlock=-size; zBlock<=size; zBlock++)
            {
                // We need some blocks
                Block block = level.create(xBlock, zBlock);
                Block east = level.create(xBlock + 1, zBlock);
                Block south = level.create(xBlock, zBlock + 1);

                // Generating them
                if(block.solid)
                {
                    if(!east.solid)
                    {
                        renderWall(xBlock + 1, xBlock +1, zBlock, zBlock + 1, 0.5);
                    }
                    if(!south.solid)
                    {
                        renderWall(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 0.5);
                    }
                }
                else
                {
                    if(east.solid)
                    {
                        renderWall(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 0.5);
                    }
                    if(south.solid)
                    {
                        renderWall(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 0.5);
                    }
                }
            }
        }
    }

    public void
    renderWall(double xLeft, double xRight, double zDistanceLeft, double zDistanceRight, double yHeight)   // Horizontal left and right position of the wall (x),
                                                                                // Distance (depth - z) and vertical height (y)
                                                                                // We are going to use our 8x8 texture's borders to draw the blocks of walls
    {
        // Make some calculations when we move (the wall is going to stay in its geographical location)
        // As before, we can 'play' with the parameter values to draw different types of walls to different locations
        double upCorrect = 0.0625;  // Correcting the 'up' so that our wall will not 'jump' with us when we jump
        double rightCorrect = 0.0625;
        double forwardCorrect = 0.0625;
        double walkCorrect = -0.0625;

        double xcLeft = ((xLeft / 2) - (right * rightCorrect)) * 2;  // c = calculate
        double zcLeft = ((zDistanceLeft / 2) - (forward * forwardCorrect)) * 2;

        double rotLeftSideX = xcLeft * cosine - zcLeft * sine;  // Rotation to the horizontal left
        double yCornerTL = ((-yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;  // Vertical Top Left Corner - it is going to depend, where we decide to put our wall (yHeight)
        double yCornerBL = ((+0.5 - yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;  // Vertical Bottom Left Corner
        double rotLeftSideZ = zcLeft * cosine + xcLeft * sine;  // (These are the same as above with the xx and yy variables)

        double xcRight = ((xRight / 2) - (right * rightCorrect)) * 2;
        double zcRigth = ((zDistanceRight / 2) - (forward * forwardCorrect)) * 2;

        double rotRightSideX = xcRight * cosine - zcRigth * sine;
        double yCornerTR = ((-yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
        double yCornerBR = ((+0.5 - yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
        double rotRightSideZ = zcRigth * cosine + xcRight * sine;

        double tex30 = 0;
        double tex40 = 8;
        double clip = 0.5;

        // Making sure before the clip, if we got a crash, just return
        if(rotLeftSideZ < clip && rotRightSideZ < clip)
        {
            return;
        }

        // Clip algorithm - Left side
        if(rotLeftSideZ < clip)
        {
            double clip0 = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
            rotLeftSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
            rotLeftSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
            tex30 = tex30 + (tex40 - tex30) * clip0;
        }

        // Clip algorithm - Right side
        if(rotRightSideZ < clip)
        {
            double clip0 = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
            rotRightSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
            rotRightSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
            tex40 = tex30 + (tex40 - tex30) * clip0;
        }

        double xPixelLeft = (rotLeftSideX / rotLeftSideZ * height + width / 2.0);  // Left edge
        double xPixelRight = (rotRightSideX / rotRightSideZ * height + width / 2.0);  // Right edge

        // What if for some reason our left side becomes greater than our right side?
        if(xPixelLeft >= xPixelRight)
        {
            return;  // Get out of this method
        }

        // Converting our computations into integers, because we need to draw to the integer pixels on our screen (not to the 17.2 position for example)
        int xPixelLeftInt = (int) (xPixelLeft);
        int xPixelRightInt = (int) (xPixelRight);

        // If the pixels are out of the screen, do not render them, but set it equals to 0
        if(xPixelLeftInt < 0)
        {
            xPixelLeftInt = 0;
        }
        if(xPixelRightInt > width)
        {
            xPixelRightInt = width;
        }

        // Making the corners
        double yPixelLeftTop = (yCornerTL / rotLeftSideZ * height + height / 2.0);
        double yPixelLeftBottom = (yCornerBL / rotLeftSideZ * height + height / 2.0);
        double yPixelRightTop = (yCornerTR / rotRightSideZ * height + height / 2.0);
        double yPixelRightBottom = (yCornerBR / rotRightSideZ * height + height / 2.0);

        // Getting our program ready for texture the wall
        double tex1 = 1 / rotLeftSideZ;
        double tex2 = 1 / rotRightSideZ;
        double tex3 = tex30 / rotLeftSideZ;
        double tex4 = tex40 / rotRightSideZ - tex3;

        // Render all the above with a trick with drawing it depending on the player's location!
        for(int x=xPixelLeftInt; x<xPixelRightInt; x++)
        {
            double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);
            double zWall = (tex1 + (tex2 - tex1) * pixelRotation);

            if(zBufferWall[x] > zWall)
            {
                continue;
            }
            zBufferWall[x] = zWall;  // Here we set that the walls won't be seen behind other walls

            int xTexture = (int) ((tex3 + tex4 * pixelRotation) / zWall);  // Horizontal texture
            double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelRotation;
            double yPixelBottom = yPixelLeftBottom + (yPixelRightBottom - yPixelLeftBottom) * pixelRotation;

            // We need to convert these also to integers
            int yPixelTopInt = (int) (yPixelTop);
            int yPixelBottomInt = (int) (yPixelBottom);

            if(yPixelTopInt < 0)
            {
                yPixelTopInt = 0;
            }
            if(yPixelBottomInt > height)
            {
                yPixelBottomInt = height;
            }

            // Do the actual render here
            for(int y=yPixelTopInt; y<yPixelBottomInt; y++)
            {
                try
                {
                    double pixelRotationY = (y - yPixelTop) / (yPixelBottom - yPixelTop);
                    int yTexture = (int) (8 * pixelRotationY);
                    // pixels[x+y*width] = xTexture * 100 + yTexture * 100 * 256;  // We can play with the color variations here!
                    pixels[x+y*width] = Texture.floor.pixels[((xTexture & 7) + 8) + (yTexture & 7) * 16];  // Adding +8 to the xTexture, because the wall's texture is started after 8 pixels on our floor.png image
                }
                catch (ArrayIndexOutOfBoundsException e)  // This is only a temporary solution for a crash!
                {
                    e.printStackTrace();
                    continue;
                }
                zBuffer[x+y*width] = 1 / (tex1 + (tex2 - tex1) * pixelRotation) * 8;  // You can play around with the number 8 and if you increase its distance
                                                                                      // with the Screen.java's render.renderWall(0, 0.5, 1.5, 0); it gets darker
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

/*
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
 */

// double up = Math.sin(game.time / 10.0) * 2;  // We are using sine, because we want to jump up and then get down
// pixels[x+y*width] = ((xPix & 15) * 16) | ((yPix & 15) * 16) << 8;



