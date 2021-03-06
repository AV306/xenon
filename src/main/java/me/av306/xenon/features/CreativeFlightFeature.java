package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.ClientPlayerTickEvents;
import me.av306.xenon.feature.IToggleableFeature;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.util.ActionResult;

public class CreativeFlightFeature extends IToggleableFeature
{
    public CreativeFlightFeature()
    {
        super( "CreativeFlight" );

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
        try
        {
            // disable jetpack and normal flyhack
            ((IToggleableFeature) Xenon.INSTANCE.featureRegistry.get( "Jetpack" )).disable();
            ((IToggleableFeature) Xenon.INSTANCE.featureRegistry.get( "Flight" )).disable();
        }
        catch ( NullPointerException ignored ) {}
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
    }
}