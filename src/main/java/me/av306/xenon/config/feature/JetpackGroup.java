package me.av306.xenon.config.feature;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries( includeAll = true )
public class JetpackGroup implements XenonConfigGroup
{
    @ConfigEntry.Boolean
    public static boolean enableNoFall = true;

    @ConfigEntriy.Boolean
    public static boolean enableJumpBoost = false;
}
