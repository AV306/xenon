package me.av306.xenon.config.feature.movement;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries( includeAll = true )
public class FullKeyboardGroup implements XenonConfigGroup
{
    @ConfigEntry.Boolean
    public static boolean enable = false;

    @ConfigEntry.Boolean
    public static boolean acceleration = true;

    @ConfigEntry.BoundedDouble( min = 0d, max = 100d )
    //@ConfigEntry.Slider
    @ConfigEntry.DoubleSliderInterval( value = 0.5d )
    public static double sensitivity = 30d;
}
