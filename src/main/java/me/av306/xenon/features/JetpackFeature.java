package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IToggleableFeature;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class JetpackFeature extends IToggleableFeature
{
    public JetpackFeature()
    {
        super( "Jetpack" );

        ClientTickEvents.END_CLIENT_TICK.register( client -> this.onEndClientTick() );
    }

    private void onEndClientTick()
    {
        // basically jump, and jump again while you;re still in midair, and repeat
        if ( Xenon.INSTANCE.client.options.jumpKey.isPressed() )
            Xenon.INSTANCE.client.player.jump();
    }

    @Override
    protected void onEnable()
    {
        // disable creative and normal flyhacks
        Xenon.INSTANCE.featureRegistry.get( "CreativeFlight" ).disable();
        Xenon.INSTANCE.featureRegistry.get( "Flight" ).disable();
    }
}