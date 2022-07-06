package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.ClientPlayerTickEvent;
import me.av306.xenon.feature.IToggleableFeature;
import net.minecraft.util.ActionResult;

public class JetpackFeature extends IToggleableFeature
{
    public JetpackFeature()
    {
        super( "Jetpack" );

        ClientPlayerTickEvent.END_PLAYER_TICK.register( this::onEndPlayerTick );
    }

    private void onEndPlayerTick()
    {
        // basically jump, and jump again while you;re still in midair, and repeat
        if ( Xenon.INSTANCE.client.options.jumpKey.isPressed() )
            Xenon.INSTANCE.client.player.jump();

        return ActionResult.PASS;
    }

    @Override
    protected void onEnable()
    {
        // disable creative and normal flyhacks
        ((IToggleableFeature) Xenon.INSTANCE.featureRegistry.get( "CreativeFlight" )).disable();
        ((IToggleableFeature) Xenon.INSTANCE.featureRegistry.get( "Flight" )).disable();
    }

    @Override
    protected void onDisable()
    {

    }
}