// Generating random levels/walls here in this class

package com.mime.minefront.level;

import java.util.Random;

public class
Level
{
    public Block[] blocks;
    public final int width, height;

    public
    Level(int width, int height)
    {
        this.width = width;
        this.height = height;
        blocks = new Block[width * height];
        Random random = new Random();
        for(int y=0; y<height; y++)  // Checking every y coordinates until height
        {
            for(int x=0; x<width; x++)  // Checking every x coordinates until width
            {
                Block block = null;  // There is no block at this moment
                // Random number generating
                if(random.nextInt(4) == 0)  // Pick a random number between 0 and 4 and if it is a 0, place a solid block
                {
                    block = new SolidBlock();
                }
                else
                {
                    block = new Block();  // Else, our block won't be solid
                }
                blocks[x + y * width] = block;
            }
        }
    }

    public Block
    create(int x, int y)
    {
        if(x < 0 || y < 0 || x >= width || y >= height)
        {
            return Block.solidWall;
        }
        return blocks[x + y * width];
    }
}









