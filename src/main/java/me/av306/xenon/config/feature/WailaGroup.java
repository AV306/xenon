package me.av306.xenon.config.feature;

import me.av306.xenon.config.XenonConfigGroup;
import me.av306.xenon.util.ScreenPosition;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries
public class WailaGroup implements XenonConfigGroup
{
	@ConfigEntry.BoundedInteger(min = 1, max = 1024)
	@ConfigEntry.Slider
	public static int interval = 3;

	@ConfigEntry.Dropdown
	public static ScreenPosition position = ScreenPosition.TOP_CENTER;
}