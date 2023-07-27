package me.av306.xenon.config.feature.render;

import me.av306.xenon.config.XenonConfigGroup;
import me.av306.xenon.util.render.ScreenPosition;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries( includeAll = true )
public class WailaGroup implements XenonConfigGroup
{
	@ConfigEntry.BoundedInteger( min = 1, max = 60 )
	@ConfigEntry.Slider
	public static int interval = 3;

	@ConfigEntry.Dropdown
	public static ScreenPosition position = ScreenPosition.TOP_CENTER;

	@ConfigEntry.BoundedInteger( min = 0, max = 1024 )
	@ConfigEntry.Slider
	public static int maxDistance = 64;

	@ConfigEntry.BoundedInteger( min = -50, max = 50 )
	@ConfigEntry.Slider
	public static int yOffset = 10;
}