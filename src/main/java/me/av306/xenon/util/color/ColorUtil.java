package me.av306.xenon.util.color;

/**
 * Helper class for converting rgb arrays to int values, and vice versa.
 */
public class ColorUtil
{
	// AARRGGBB
	public static final int WHITE = 0xFFFFFF;
	public static final int BLACK = 0x000000;
	public static final int RED = 0xFF0000;
	public static final int GREEN = 0x00FF00;
	public static final int BLUE = 0x0000FF;
	
	public static int rgbToInt( int r, int g, int b ) { return r << 16 | g << 8 | b; }

  	public static int argbToInt( int a, int r, int g, int b )
    {
      return a << 24 | r << 16 | g << 8 | b;
    }

	public static int[] intToRgb( int rgb )
	{
		return new int[]{ rgb >> 16 & 0xFF, rgb >> 8 & 0xFF, rgb & 0xFF };
	}

	public static int[] intToArgb( int argb )
	{
		return new int{ argb >> 24 & 0xFF, argb >> 16 & 0xFF, argb >> 8 & 0xFF, argb & 0xFF };
	}
}