package me.av306.xenon.config.feature;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.*;
import me.lortseam.completeconfig.api.ConfigEntry.*;

@ConfigEntries
public class WailaGroup implements XenonConfigGroup
{
	@ConfigEntry.BoundedInteger(min = 1, max = 1024)
	@ConfigEntry.Slider
	public static int interval = 3;
}