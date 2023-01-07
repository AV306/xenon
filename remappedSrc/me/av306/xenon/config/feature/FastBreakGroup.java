package me.av306.xenon.config.feature;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;

@ConfigEntries( includeAll = true )
public class FastBreakGroup implements XenonConfigGroup
{
    public static boolean onlyRemoveCooldown = false;
}
