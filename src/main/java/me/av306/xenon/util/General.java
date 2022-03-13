package me.av306.xenon.util;

public class General
{
    public static int rgbToInt( int r, int g, int b )
    {
        return r << 16 | g << 8 | b;
    }

    public static int[] intToRgb( int rgb )
    {
        return new int[]{ rgb >> 16, rgb >> 8, rgb };
    }
}
