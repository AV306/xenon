package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.event.ClientPlayerTickEvents;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import net.minecraft.util.ActionResult;

public class NoFallDamageFeature extends IToggleableFeature
{
    public NoFallDamageFeature()
    {
        super( "NoFallDamage", "nofall", "nf", "nfd" );

        ClientPlayerTickEvents.END_PLAYER_TICK.register( this::onEndPlayerTick );
    }

    private ActionResult onEndPlayerTick()
    {
        if ( !this.isEnabled ) return ActionResult.PASS;

        ClientPlayerEntity player = Xenon.INSTANCE.client.player;

        assert player != null;
        if ( player.fallDistance <= (player.isFallFlying() ? 1 : 2) )
            return ActionResult.PASS;

        if(
            player.isFallFlying() && player.isSneaking()
			&& !isFallingFastEnoughToCauseDamage(player)
        ) return ActionResult.PASS;
		
		player.networkHandler.sendPacket( new OnGroundOnly( true ) );

        return ActionResult.PASS;
	}
	
	private boolean isFallingFastEnoughToCauseDamage( ClientPlayerEntity player )
	{
		return player.getVelocity().y < -0.5;
	}

    @Override
    protected void onEnable()
    {

    }

    @Override
    protected void onDisable()
    {

    }
}