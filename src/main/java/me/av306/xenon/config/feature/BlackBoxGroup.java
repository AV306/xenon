package me.av306.xenon.config.feature;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries( includeAll = true )
public class BlackBoxGroup implements XenonConfigGroup
{

	@ConfigEntry.Boolean
	public static boolean showInFeatureList = true;

	@ConfigEntry.Boolean
	public static boolean reEnableOnWorldEnter = false;

	@ConfigEntry.BoundedInteger( min = 1, max = 100 )
	@ConfigEntry.Slider
	public static int trackingLogInterval = 5;

	@ConfigEntry.BoundedInteger( min = 1, max = 100 )
	@ConfigEntry.Slider
	public static int writeInterval = 5;

	@ConfigEntry.Boolean
	//@ConfigEntry.Checkbox
	public static boolean writeEntireQueue = true;

	@ConfigEntry.BoundedInteger( min = 1, max = 100 )
	@ConfigEntry.Slider
	public static int bufferSize = 5;
}
