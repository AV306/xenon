package me.av306.xenon.features.movement;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.CreativeFlightGroup;
import me.av306.xenon.event.ClientPlayerTickEvents;
import me.av306.xenon.feature.IToggleableFeature;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.util.ActionResult;

public class CreativeFlightFeature extends IToggleableFeature
{
    public CreativeFlightFeature()
    {
        super( "CreativeFlight", "cf" );

        ClientPlayerTickEvents.END_PLAYER_TICK.register( this::onEndPlayerTick );
    }

    private ActionResult onEndPlayerTick()
    {
        if ( !this.isEnabled ) return ActionResult.PASS;

        // force flying
        Xenon.INSTANCE.client.player.getAbilities().allowFlying = true;

        return ActionResult.PASS;
    }

    @Override
    protected void onEnable()
    {
        // disable jetpack and normal flyhack
        ((IToggleableFeature) Xenon.INSTANCE.featureRegistry.get( "jetpack" )).disable();
        //((IToggleableFeature) Xenon.INSTANCE.featureRegistry.get( "Flight" )).disable();

        // enable no fall damage if wanted
        if ( CreativeFlightGroup.enableNoFall )
            Xenon.INSTANCE.featureRegistry.get( "nofalldamage" ).enable();
    }

    @Override
    protected void onDisable()
    {
        ClientPlayerEntity player = Xenon.INSTANCE.client.player;
        PlayerAbilities abilities = player.getAbilities();
        boolean creative = player.isCreative();

        // stop flying unless player is actually creative AND already flying
        abilities.flying = creative && !player.isOnGround();
        abilities.allowFlying = creative;

        ((IToggleableFeature) Xenon.INSTANCE.featureRegistry.get( "nofalldamage" )).disable();
    }
}