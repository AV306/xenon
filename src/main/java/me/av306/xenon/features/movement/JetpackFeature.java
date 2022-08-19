package me.av306.xenon.features.movement;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.JetpackGroup;
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
        // disable creative and normal flyhacks
        ((IToggleableFeature) Xenon.INSTANCE.featureRegistry.get( "creativeflight" )).disable();
        //((IToggleableFeature) Xenon.INSTANCE.featureRegistry.get( "flight" )).disable();

        // enable no fall damage if wanted
        if ( JetpackGroup.enableNoFall )
            Xenon.INSTANCE.featureRegistry.get( "nofalldamage" ).enable();

        if ( JetpackGroup.enableJumpBoost )
            // add legacy highjump-like stuff
            Xenon.INSTANCE.featureRegistry.get( "boost" ).enable();
    }

    @Override
    protected void onDisable()
    {
        ((IToggleableFeature) Xenon.INSTANCE.featureRegistry.get( "nofalldamage" )).disable();
        ((IToggleableFeature) Xenon.INSTANCE.featureRegistry.get( "boost" )).disable();
    }
}