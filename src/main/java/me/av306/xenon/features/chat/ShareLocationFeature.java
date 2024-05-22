package me.av306.xenon.features.chat;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IFeature;
import me.av306.xenon.util.text.TextFactory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;

public class ShareLocationFeature extends IFeature
{
	public ShareLocationFeature() { super( "ShareLocation" ); }


	@Override
	protected void onEnable()
	{
		// get the player's current position
		assert Xenon.INSTANCE.client.player != null;
		Vec3d currentPos = Xenon.INSTANCE.client.player.getPos();

		// get the player's current dimension
		//assert Xenon.INSTANCE.client.world != null;
		//String dim = Xenon.INSTANCE.client.world.getChunk( Xenon.INSTANCE.client.player.getBlockPos()).bi

		// round and display
		String loc = String.format(
				"[%d %d %d](%s)",
				Math.round( currentPos.getX() ),
				Math.round( currentPos.getY() ),
				Math.round( currentPos.getZ() ),
				RegistryKeys.toDimensionKey( Xenon.INSTANCE.client.world.getRegistryKey() ).getValue().toString()
		);

		Xenon.INSTANCE.client.getNetworkHandler().sendChatMessage( loc );
	}
}