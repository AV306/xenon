package me.av306.xenon.config.feature.render;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;
import me.shedaniel.math.Color;

@ConfigEntries( includeAll = true )
public class ProximityRadarGroup implements XenonConfigGroup
{
    @ConfigEntry.BoundedInteger( min = 1, max = 100 )
    public static int range = 3;

    /*@ConfigEntry.BoundedInteger( min = 1, max = 100 )
    @ConfigEntry.Slider
    public static int interval = 5;*/

    @ConfigEntry.Boolean
    public static boolean showBox = true;

    @ConfigEntry.Boolean
    public static boolean showTracer = true;

    @ConfigEntry.Boolean
    public static boolean detectItems = true;

    @ConfigEntry.Boolean
    public static boolean detectPlayers = true;

    @ConfigEntry.Color( alphaMode = false )
    public static Color playerBoxColor = Color.ofRGB( 255, 255, 255 );

    @ConfigEntry.Color( alphaMode = false )
    public static Color hostileBoxColor = Color.ofRGB( 255, 0, 0 );

    @ConfigEntry.Color( alphaMode = false )
    public static Color itemBoxColor = Color.ofRGB( 116, 190, 207 );

}
