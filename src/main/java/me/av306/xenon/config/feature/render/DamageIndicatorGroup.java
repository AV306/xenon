package me.av306.xenon.config.feature.render;

import me.av306.xenon.config.XenonConfigGroup;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;

@ConfigEntries( includeAll = true )
public class DamageIndicatorGroup implements XenonConfigGroup
{
    // Duration that the indicator is shown for before it fades
    public static float indicatorDurationMillis = 1000f;

    // Time taken for the indicator to fade
    public static float indicatorFadeDurationMillis = 100f;

    // Indicator offset from screen edge
    @ConfigEntry.BoundedInteger( min = 0, max = 100 )
    @ConfigEntry.Slider
    public static int indicatorOffset = 20;

    // Width of indicator relative to screen ([0...1])
    public static float indicatorSizeFactor = 0.5f;

    // Height of indicator relative to screen ([0...1])
    //public static float indicatorHeightFactor = 0.7f;

    @ConfigEntry.BoundedInteger( min = 1, max = 100 )
    @ConfigEntry.Slider
    public static int indicatorHeight = 50;
}
