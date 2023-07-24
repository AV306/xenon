package me.av306.xenon.config.feature.render;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries( includeAll = true )
public class FlightHudGroup
{
    @ConfigEntry.Boolean
    public static boolean warnStall = true;
    @ConfigEntry.BoundedFloat( min = 0, max = 100 )
    public static float stallVelocityThreshold = 1f;

    @ConfigEntry.Boolean
    public static boolean warnTerrain = true;
    @ConfigEntry.BoundedInteger( min = 0, max = 400 )
    @ConfigEntry.Slider
    public static int terrainThreshold = 50;

    @ConfigEntry.Boolean
    public static boolean warnPullUp = true;
    @ConfigEntry.BoundedFloat( min = -90f, max = 0 )
    public static float pullUpPitchThreshold = -70f;
}
