package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.ClientPlayerTickEvents;
import me.av306.xenon.feature.IToggleableFeature;
import net.minecraft.util.ActionResult;

public class JetpackFeature extends IToggleableFeature
{
    public JetpackFeature()
    {
        super( "Jetpack" );

        ClientPlayerTickEvents.END_PLAYER_TICK.register( this::onEndPlayerTick );
    }

    private ActionResult onEndPlayerTick()
    {
        if ( !this.isEnabled ) return ActionResult.PASS;

        // basically jump, and jump again while you;re still in midair, and repeat
        if ( Xenon.INSTANCE.client.options.jumpKey.isPressed() )
            Xenon.INSTANCE.client.player.jump();

        return ActionResult.PASS;
    }

    @Override
    protected void onEnable()
    {
        try
        {
            // disable creative and normal flyhacks
            ((IToggleableFeature) Xenon.INSTANCE.featureRegistry.get( "CreativeFlight" )).disable();
            ((IToggleableFeature) Xenon.INSTANCE.featureRegistry.get( "Flight" )).disable();
        }
        catch ( NullPointerException ignored )
        {
            
        }
    }

    @Override
    protected void onDisable()
    {

    }
}