package me.av306.xenon.config.feature;

import me.lortseam.completeconfig.api.*;

@ConfigEntries
public class WailaGroup implements ConfigGroup
{
	@ConfigEntry.BoundedInt(min = 1, max = 1024)
	@ConfigEntry.Slider
	public static int interval = 3;
}