package me.av306.xenon.features.movement;

import me.av306.xenon.feature.IToggleableFeature;

public class FundyIcePhysicsFeature extends IToggleableFeature
{
    public FundyIcePhysicsFeature()
    {
        super( "Fundy Ice Physics", "fip", "icephysics", "ice" );
    }

    @Override
    protected void onEnable()
    {
        //EventFields.SLIPPERINESS_OVERRIDE = 0.9999f;
    }

    @Override
    protected void onDisable()
    {
        //EventFields.SLIPPERINESS_OVERRIDE = -1f;
    }
}
