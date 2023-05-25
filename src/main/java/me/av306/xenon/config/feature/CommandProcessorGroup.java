package me.av306.xenon.config.feature;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries( includeAll = true )
public class CommandProcessorGroup implements XenonConfigGroup
{
    @ConfigEntry.Boolean
    public static boolean reEnableOnWorldEnter = true;

    public static String prefix = "!";

    @ConfigEntry.Boolean
    public static boolean warn = true;
}
