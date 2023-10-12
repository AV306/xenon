package me.av306.xenon.config.feature.render;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.*;

@ConfigEntries( includeAll = true )
public class TakePanoramaGroup implements XenonConfigGroup
{
	@ConfigEntry.BoundedInteger( min = 4, max = 8192 )
	@ConfigEntry.Slider
	public static int resolution = 4096;
}