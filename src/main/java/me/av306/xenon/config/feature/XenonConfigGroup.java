package me.av306.xenon.config.feature;

import me.lortseam.completeconfig.api.ConfigGroup;

public interface XenonConfigGroup extends ConfigGroup
{
    default String getId()
    {
        return getClass().getSimpleName().toLowerCase();
    }
}
