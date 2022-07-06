package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IToggleableFeature;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerAbilities;

public class CreativeFlightFeature extends IToggleableFeature
{
    public CreativeFlightFeature()
    {
        super( "CreativeFlight" );

        ClientPlayerTickEvent.END_PLAYER_TICK.register( client -> this.onEndPlayerTick() );
    }

    private void onEndPlayerTick()
    {
        // force flying
        Xenon.INSTANCE.client.player.getAbilities().allowFlying = true;
    }

    @Override
    protected void onEnable()
    {
        // disable jetpack and normal flyhack
        ((IToggleableFeature) Xenon.INSTANCE.featureRegistry.get( "Jetpack" )).disable();
        ((IToggleableFeature) Xenon.INSTANCE.featureRegistry.get( "Flight" )).disable();
    }

    @Override
    protected void onDisable()
    {
        ClientPlayerEntity player = Xenon.INSTANCE.client.player;
        PlayerAbilities abilities = player.getAbilities();
        boolean creative = player.isCreative();

        // stop flying unless player is actually creative AND already flying
        abilities.flying = creative && !player.isOnGround();
        abilities.allowFlight = creative;
    }
}