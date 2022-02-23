package com.mime.minefront;

import java.io.*;
import java.util.Properties;

public class
Configuration
{
    Properties properties = new Properties();

    public void
    saveConfiguration(String key, int value)
    {
        String path = "res/settings/config.xml";
        try
        {
            File file = new File(path);
            boolean exist = file.exists();
            if(!exist)
            {
                file.createNewFile();
            }
            OutputStream write = new FileOutputStream(path);
            properties.setProperty(key, Integer.toString(value));
            properties.storeToXML(write, "Resolution");  // We can choose 'null' to 'comment' if we do not want to comment it
        }
        catch (Exception e)
        {
            e.printStackTrace();  // We are printing the stack trace to see that if it is crashed
        }
    }

    public void
    loadConfiguration(String path)
    {
        try
        {
            InputStream read = new FileInputStream(path);
            properties.loadFromXML(read);  // Important thing is to choose loadFromXML() here instead of load(), because we also chose storeToXML()!
            String width = properties.getProperty("width");
            String height = properties.getProperty("height");
            setResolution(Integer.parseInt(width), Integer.parseInt(height));
            read.close();  // Unload and delete that file from the memory and storage
        }
        catch(FileNotFoundException e)
        {
            saveConfiguration("width", 800);
            saveConfiguration("height", 600);
            loadConfiguration(path);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void
    setResolution(int width, int height)
    {
        Display.width = width;
        Display.height = height;
    }
}















