package me.av306.xenon.config.feature.render;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;
import me.shedaniel.math.Color;

@ConfigEntries( includeAll = true )
public class ProximityRadarGroup implements XenonConfigGroup
{
    @ConfigEntry.BoundedInteger( min = 1, max = 1000 )
    public static int playerRange = 64;
    @ConfigEntry.Boolean
    public static boolean detectPlayers = true;
    @ConfigEntry.Boolean
    public static boolean showPlayerBox = true;
    @ConfigEntry.Boolean
    public static boolean showPlayerTracer = true;
    @ConfigEntry.Color( alphaMode = false )
    public static Color playerBoxColor = Color.ofRGB( 255, 255, 255 );


    @ConfigEntry.BoundedInteger( min = 1, max = 1000 )
    public static int hostileRange = 20;
    @ConfigEntry.Boolean
    public static boolean detectHostiles = true;
    @ConfigEntry.Boolean
    public static boolean detectZombifiedPiglins = false;
    @ConfigEntry.Boolean
    public static boolean detectHoglins = true;
    @ConfigEntry.Boolean
    public static boolean showHostileBox = true;
    @ConfigEntry.Boolean
    public static boolean showHostileTracer = true;
    @ConfigEntry.Color( alphaMode = false )
    public static Color hostileBoxColor = Color.ofRGB( 255, 0, 0 );

    @ConfigEntry.BoundedInteger( min = 1, max = 1000 )
    public static int itemRange = 5;
    @ConfigEntry.Boolean
    public static boolean detectItems = true;

    @ConfigEntry.Boolean
    public static boolean showItemBox = true;
    @ConfigEntry.Boolean
    public static boolean showItemTracer = true;
    @ConfigEntry.Color( alphaMode = false )
    public static Color itemBoxColor = Color.ofRGB( 116, 190, 207 );

    @ConfigEntry.BoundedInteger( min = 1, max = 1000 )
    public static int projectileRange = 20;
    @ConfigEntry.Boolean
    public static boolean detectProjectiles = true;
    @ConfigEntry.Boolean
    public static boolean showProjectileBox = true;
    @ConfigEntry.Boolean
    public static boolean showProjectileTracer = true;
    @ConfigEntry.Color( alphaMode = false )
    public static Color projectileBoxColor = Color.ofRGB( 255, 255, 255 );
}
