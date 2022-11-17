package me.av306.xenon.util.render;

/**
 * Enum for positions on-screen where objects can be drawn.
 * Used instead of a set of final ints because:
 * - I wrote it as an enum
 * - Pretty much everything here uses this enum
 * - My own semantics (looks better in a switch)
 *
 * use fromInt() to convert :)
 */
public enum ScreenPosition
{
	TOP_LEFT,
	TOP_CENTER,
  	TOP_RIGHT,
  	BOTTOM_LEFT,
	BOTTOM_CENTER,
  	BOTTOM_RIGHT;

	public static ScreenPosition fromInt( int i )
  	{
			return values()[i];
		}
}
