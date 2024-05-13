package me.av306.xenon;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.PacketByteBuf;

public class MainInitializer implements ModInitializer
{
	@Override
	public void onInitialize()
	{
		Xenon.INSTANCE.LOGGER.info( "Doing server-side initialisation..." );

		// TODO
		ServerPlayConnectionEvents.JOIN.register( (handler, sender, server) ->
		{
			PacketByteBuf pak = PacketByteBufs.create();
			pak.writeString( "timer" );
			sender.sendPacket( Xenon.INSTANCE.BLOCKED_FEATURE_PACKET, pak );
		} );
	}
}
