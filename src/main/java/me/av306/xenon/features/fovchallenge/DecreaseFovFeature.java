package me.av306.xenon.features.fovchallenge;

import me.av306.xenon.event.EventFields;
import me.av306.xenon.feature.IFeature;

public class DecreaseFovFeature extends IFeature
{
    public DecreaseFovFeature()
    {
        super( "DecreaseFov", "df" );
    }

    @Override
    protected void onEnable()
    {
        EventFields.FOV_MODIFIER -= 10;
    }
}
