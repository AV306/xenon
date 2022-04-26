package me.av306.xenon.config.feature;

import me.lortseam.completeconfig.api.*;
import me.lortseam.completeconfig.api.ConfigEntry.*;

@ConfigEntries
public class TakePanoramaGroup implements ConfigGroup
{
	@ConfigEntry.BoundedInt(min = 4, max = 1024)
	@ConfigEntry.Slider
	public static int resolution = 512;
}