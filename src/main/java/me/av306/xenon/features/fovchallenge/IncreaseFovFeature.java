package me.av306.xenon.features.fovchallenge;

import me.av306.xenon.event.EventFields;
import me.av306.xenon.feature.IFeature;

public class IncreaseFovFeature extends IFeature
{
    public IncreaseFovFeature()
    {
        super( "IncreaseFov", "if" );
    }

    @Override
    protected void onEnable()
    {
        EventFields.FOV_MODIFIER += 10;
    }
}
