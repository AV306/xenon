package me.av306.xenon.config.feature;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries( includeAll = true )
public class ProximityRadarGroup implements XenonConfigGroup
{
    @ConfigEntry.BoundedInteger( min = 1, max = 100 )
    public static int range = 3;

    @ConfigEntry.BoundedInteger( min = 1, max = 100 )
    @ConfigEntry.Slider
    public static int interval = 5;

    @ConfigEntry.Boolean
    public static boolean showBox = true;
}
