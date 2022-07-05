package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IToggleableFeature;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

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
        Xenon.INSTANCE.featureRegistry.get( "Jetpack" ).disable();
        Xenon.INSTANCE.featureRegistry.get( "CreativeFlight" ).disable();
    }

    @Override
    protected void onDisable()
    {

    }
}