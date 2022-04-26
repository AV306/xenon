package me.av306.xenon.config.feature;

import me.lortseam.completeconfig.api.*; 
import me.lortseam.completeconfig.api.ConfigEntry.*;
import me.av306.xenon.util.ScreenPosition;

@ConfigEntries
public class FeatureListGroup implements ConfigGroup
{
	@ConfigEntry
	public static boolean showVersion = true;

	@ConfigEntry
	@ConfigEntry.DropDown
	public static ScreenPosition position = ScreenPosition.TOP_RIGHT; // hope this works
}