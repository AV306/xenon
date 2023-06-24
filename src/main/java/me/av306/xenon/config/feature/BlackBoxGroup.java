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
	public static boolean enableOnWorldEnter = true;
}
