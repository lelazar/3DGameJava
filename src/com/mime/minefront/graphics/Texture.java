package com.mime.minefront.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class
Texture
{
    public static Render floor = loadBitmap("/textures/floor.png");

    public static Render loadBitmap(String fileName)
    {
        try {  // Try to load the image "/textures/floor.png" (from fileName)
            BufferedImage image = ImageIO.read(Texture.class.getResource(fileName));  // fileName variable, so if we change the location or the image, it can be changed easily
            int width = image.getWidth();
            int height = image.getHeight();
            Render result = new Render(width, height);
            image.getRGB(0, 0, width, height, result.pixels, 0, width);  // Read the Java Documentation about getRGB()!
            return result;
        } catch (Exception e) {  // If it cannot be loaded for any reason...
            System.out.println("CRASH!");
            throw new RuntimeException(e);
        }
    }
}








