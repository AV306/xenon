package me.av306.xenon;

import me.av306.xenon.packets.OptInPacketPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;

public class MainInitializer implements ModInitializer
{
	@Override
	public void onInitialize()
	{
		//if ( )
		Xenon.INSTANCE.LOGGER.info( "Doing logical server-side initialisation..." );

		// TODO
		ServerPlayConnectionEvents.JOIN.register( (handler, sender, server) ->
		{
			// Uncomment this line and replace the placeholder text
			// with the name of the feature you wish to block.
			// Timer and ProxRadar are blocked by default, and will be
			// removed in a future version.
			//sender.sendPacket( new OptOutPacketPayload( "[FEATURE_NAME_HERE]" ) );
		} );
	}
}
