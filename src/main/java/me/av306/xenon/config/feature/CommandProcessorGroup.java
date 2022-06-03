package me.av306.xenon.config.feature;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries
public class CommandProcessorGroup implements XenonConfigGroup
{
    public static String prefix = "!";

    @ConfigEntry.Boolean
    public static boolean warn = true;
}
