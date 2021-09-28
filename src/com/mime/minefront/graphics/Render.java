package com.mime.minefront.graphics;

public class
Render
{
    public final int width;
    public final int height;
    public final int[] pixels;

    public
    Render(int width, int height)  // Constructor -> inherits the width and height from WIDTH and HEIGHT
    {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];  // Assigning 480,000 variables to an Array called pixels (800 * 600)
    }

    public void
    draw(Render render, int xOffset, int yOffset)  // Rendering and drawing pixels to the screen
    {
        for(int y=0; y<render.height; y++)  // Doing the draw until it covers the whole height
        {
            int yPix = y + yOffset;  // Adds whatever value of the y's offset to y
            if(yPix < 0 || yPix >= height) continue;

            for(int x=0; x<render.width; x++)  // Doing the draw until it covers the whole width
            {
                int xPix = x + xOffset;  // Adds whatever value of the x's offset to x
                if(xPix < 0 || xPix >= width) continue;

                int alpha = render.pixels[x + y * render.width];

                // alpha support
                if(alpha > 0) {  // We create a variable called alpha and checking if we even want to render anything (alpha > 0) and if so, we are rendering => Performance?
                    pixels[xPix + yPix * width] = alpha;
                }
            }
        }
    }
}












