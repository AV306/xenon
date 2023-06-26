package me.av306.xenon.config.feature.render;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries( includeAll = true )
public class ZoomGroup implements XenonConfigGroup
{
	@ConfigEntry.BoundedDouble( min = 1.5d, max = 300d )
	//@ConfigEntry.DoubleSliderInterval( 0.5d )
	//@ConfigEntry.Slider
	public static double minZoom = 2d;

    @ConfigEntry.BoundedDouble( min = 1.5d, max = 300d )
	//@ConfigEntry.DoubleSliderInterval( 0.5d )
	//@ConfigEntry.Slider
	public static double maxZoom = 60d;

	@ConfigEntry.BoundedDouble( min = 0.05d, max = 10d )
	//@ConfigEntry.DoubleSliderInterval( 0.5d )
	//@ConfigEntry.Slider
	public static double scrollInterval = 0.1d;
}