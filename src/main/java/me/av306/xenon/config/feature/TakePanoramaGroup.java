package me.av306.xenon.config.feature;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.*;
import me.lortseam.completeconfig.api.ConfigEntry.*;

@ConfigEntries( includeAll = true )
public class TakePanoramaGroup implements XenonConfigGroup
{
	@ConfigEntry.BoundedInteger(min = 4, max = 1024)
	@ConfigEntry.Slider
	public static int resolution = 512;
}