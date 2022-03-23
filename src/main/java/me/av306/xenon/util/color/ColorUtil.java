package me.av306.xenon.util.color;

public class ColorUtil
{
		public static final int WHITE = 0xFFFFFF;
		public static final int BLACk = 0x000000;
		public static final int RED = 0xFF0000;
		public static final int GREEN = 0x00FF00;
		public static final int BLUE = 0x0000FF;
	
	  public static int rgbToInt( int r, int g, int b )
    {
      return r << 16 | g << 8 | b;
  	}

	  public static int argbToInt( int a, int r, int g, int b )
    {
      return a << 24 | r << 16 | g << 8 | b;
    }
}