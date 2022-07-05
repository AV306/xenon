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

        ClientTickEvents.END_CLIENT_TICK.register( client -> this.onEndClientTick() );
    }

    private void onEndClientTick()
    {
        // force flying
        Xenon.INSTANCE.client.player.getAbilities().allowFlying = true;
    }

    @Override
    protected void onEnable()
    {
        // disable jetpack and normal flyhack
        Xenon.INSTANCE.featureRegistry.get( "Jetpack" ).disable();
        Xenon.INSTANCE.featureRegistry.get( "Flight" ).disable();
    }

    @Override
    protected void onDisable()
    {
        ClientPlayerEntity playerEntity = Xenon.INSTANCE.client.player;
        PlayerAbilities = playerEntity.getAbilities();
        boolean creative = player.isCreative();

        // stop flying unless player is actually creative AND already flying
        abilities.flying = creative && !player.isOnGround();
        abilities.allowFlight = creative;
    }
}