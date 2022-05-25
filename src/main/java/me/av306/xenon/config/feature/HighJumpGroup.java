package me.av306.xenon.config.feature;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries
public class HighJumpGroup implements XenonConfigGroup
{
    @ConfigEntry.BoundedFloat( min = -1f, max = 10f )
    public static float height = 0.0f;
}
