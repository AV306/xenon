package me.av306.xenon.config.feature;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries( includeAll = true )
public class HighJumpGroup implements XenonConfigGroup
{
    @ConfigEntry.BoundedFloat( min = -1f, max = 200f )
    public static float height = 0.0f;

    @ConfigEntry.BoundedFloat( min = 0f, max = 2f )
    public static float multiplier = 0.1f;
}
