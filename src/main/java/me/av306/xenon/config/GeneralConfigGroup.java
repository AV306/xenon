package me.av306.xenon.config;

import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries( includeAll = true )
public class GeneralConfigGroup implements XenonConfigGroup
{
    @ConfigEntry.Boolean
    @ConfigEntry( requiresRestart = true )
    public static boolean lazyDfu = true;

    @ConfigEntry.BoundedInteger( min = 1, max = 512 )
    @ConfigEntry( requiresRestart = false )
    @ConfigEntry.Slider
    public static int debugCrosshairSize = 10;

    @ConfigEntry.BoundedInteger( min = 1, max = 64 )
    @ConfigEntry( requiresRestart = false )
    @ConfigEntry.Slider
    public static int crosshairSize = 1;
}
