package me.av306.xenon.config.feature;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries
public class TimerGroup implements XenonConfigGroup
{
    @ConfigEntry.Slider
    @ConfigEntry.BoundedFloat( min = 0.5F, max = 10.0F )
    public static float timerSpeed = 1.0F;
}
