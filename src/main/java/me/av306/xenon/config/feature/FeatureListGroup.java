package me.av306.xenon.config.feature;

import me.av306.xenon.config.XenonConfigGroup;
import me.av306.xenon.util.ScreenPosition;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries( includeAll = true )
public class FeatureListGroup implements XenonConfigGroup
{
	public static boolean showVersion = true;

	public static boolean shadow = false;

	@ConfigEntry.Dropdown
	public static ScreenPosition position = ScreenPosition.TOP_RIGHT;
}