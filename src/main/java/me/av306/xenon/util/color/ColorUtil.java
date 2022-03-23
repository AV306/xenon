package me.av306.xenon.util.color;

public class ColorUtil
{
	  public static int rgbToInt( int r, int g, int b )
    {
      return r << 16 | g << 8 | b;
  	}

	  public static int argbToInt( int a, int r, int g, int b )
    {
      return a << 24 | r << 16 | g << 8 | b;
    }
}