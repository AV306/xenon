package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IFeature;

import net.minecraft.world.dimension.DimensionType;
import net.minecraft.util.math.Vec3d;

public class ShareLocationFeature extends IFeature
{
	public ShareLocationFeature() { super( "ShareLocation" ); }

	/*private String formatDimType( DimensionType dim )
	{
		// lasagna code to convert dim type to string
		if ( dim.equals( DimensionType.OVERWORLD ) ) return "Overworld";
		else if ( dim.equals( DimensionType.OVERWORLD_CAVES ) ) return "Overworld Caves";
		else if ( dim.equals( DimensionType.THE_END ) ) return "The End";
		else if ( dim.equals( DimensionType.THE_NETHER ) ) return "Nether";
	}*/

	@Override
	public void onEnable()
	{
		// get the player's current position
		Vec3d currentPos = Xenon.INSTANCE.client.player.getPos();

		// get the player's current dimension
		DimensionType dim = Xenon.INSTANCE.client.world.getDimension();

		String text = "[" + currentPos.getX() + ", " + 
			currentPos.getY() + ", " + 
			currentPos.getZ() + "]";

		Xenon.INSTANCE.client.player.sendChatMessage( text );
	}

	@Override
	public void onDisable() {}
}