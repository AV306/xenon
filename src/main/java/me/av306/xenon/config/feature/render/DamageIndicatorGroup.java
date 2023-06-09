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
}
