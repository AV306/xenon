package me.av306.xenon.config;

import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries( includeAll = true )
public class GeneralConfigGroup implements XenonConfigGroup
{
    @ConfigEntry.Boolean
    @ConfigEntry( requiresRestart = true )
    public static boolean lazyDfu = true;
}
