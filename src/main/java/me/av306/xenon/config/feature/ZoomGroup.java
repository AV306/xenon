package me.av306.xenon.config.feature;

import me.av306.xenon.config.XenonConfigGroup;
import me.av306.xenon.util.ScreenPosition;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries( includeAll = true )
public class ZoomGroup implements XenonConfigGroup
{
	@ConfigEntry.BoundedDouble( min = 2d, max = 100d )
	@ConfigEntry.Slider
	public static Double minZoom = 2d;

    @ConfigEntry.BoundedDouble( min = 2d, max = 100d )
    public static Double maxZoom = 60d;
}