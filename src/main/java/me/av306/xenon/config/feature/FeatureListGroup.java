package me.av306.xenon.config.feature;

import me.av306.xenon.Xenon;
import me.lortseam.completeconfig.api.*;
import me.lortseam.completeconfig.api.ConfigEntry.*;
import me.av306.xenon.util.ScreenPosition;
import org.lwjgl.glfw.GLFW;

@ConfigEntries
public class FeatureListGroup implements XenonConfigGroup
{
	public static boolean showVersion = true;

	@ConfigEntry.Dropdown
	public static ScreenPosition position = ScreenPosition.TOP_RIGHT; // hope this works
}