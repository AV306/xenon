package me.av306.xenon.config;

import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries( includeAll = true )
public class GeneralConfigGroup implements XenonConfigGroup
{
    //@ConfigEntry.Boolean
    //public static boolean checkForUpdates = true;
    
    @ConfigEntry.Boolean
    public static boolean lazyDfu = true;

    @ConfigEntry.BoundedInteger( min = 1, max = 512 )
    @ConfigEntry.Slider
    public static int debugCrosshairSize = 10;

    @ConfigEntry.Boolean
    public static boolean enableTimer = true;

    @ConfigEntry.Boolean
    public static boolean infiniteChatLength = true;

    @ConfigEntry.Boolean
    public static boolean allowPortalGuis = true;
}
