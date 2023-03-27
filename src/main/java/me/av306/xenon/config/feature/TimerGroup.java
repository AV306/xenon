package me.av306.xenon.config.feature;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries( includeAll = true )
public class TimerGroup implements XenonConfigGroup
{
    @ConfigEntry( comment = "Warning: Timer is considered a \"hack\" by many communities; it's only here because it's genuinely useful in situations where it is allowed. Use at your own risk." )
    @ConfigEntry.BoundedFloat( min = 0.5F, max = 10.0F )
    public static Float timerSpeed = 1.0F;
}