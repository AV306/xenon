package me.av306.xenon.config.feature;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.*;

@ConfigEntries( includeAll = true )
public class QuickChatGroup implements XenonConfigGroup
{
	public static String message = "default";
}