package me.av306.xenon.features;

import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.event.ClientPlayerTickEvents;

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

        if ( player.fallDistance <= (player.isFallFlying() ? 1 : 2) )
            return ActionResult.PASS;

        if(player.isFallFlying() && player.isSneaking()
			&& !isFallingFastEnoughToCauseDamage(player))
			return ActionResult.PASS;
		
		player.networkHandler.sendPacket(new OnGroundOnly(true));
	}
	
	private boolean isFallingFastEnoughToCauseDamage(ClientPlayerEntity player)
	{
		return player.getVelocity().y < -0.5;
	}
}