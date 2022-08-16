package me.av306.xenon.config.feature;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries( includeAll = true )
public class ZoomGroup implements XenonConfigGroup
{
	@ConfigEntry.BoundedDouble( min = 1.5d, max = 100d )
	//@ConfigEntry.Slider
	public static double minZoom = 2d;

    @ConfigEntry.BoundedDouble( min = 1.5d, max = 100d )
    public static double maxZoom = 60d;
}