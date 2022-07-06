package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IToggleableFeature;

public class FlightFeature extends IToggleableFeature
{
    // TODO: Implement

    public FlightFeature()
    {
        super( "Flight" );
    }

    @Override
    protected void onEnable()
    {
        ((IToggleableFeature) Xenon.INSTANCE.featureRegistry.get( "Jetpack" )).disable();
        ((IToggleableFeature) Xenon.INSTANCE.featureRegistry.get( "CreativeFlight" )).disable();
    }

    @Override
    protected void onDisable()
    {

    }
}