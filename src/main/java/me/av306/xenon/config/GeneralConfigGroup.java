package me.av306.xenon.config;

import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries( includeAll = true )
public class GeneralConfigGroup implements XenonConfigGroup
{
    @ConfigEntry.BoundedInteger( min = 0, max = 100 )
    @ConfigEntry.Slider
    public static int interval = 5;
}
