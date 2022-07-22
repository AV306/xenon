package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IFeature;
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

		if ( dim.bedWorks() ) return "Overworld"; // beds only work in OW
		else if ( dim.coordinateScale() != 1 ) return "Nether"; // Nether has x8 CS
		else return "End"; // does not support modded dims

		// TODO: figure registries out
	}*/

	@Override
	protected void onEnable()
	{
		// get the player's current position
		assert Xenon.INSTANCE.client.player != null;
		Vec3d currentPos = Xenon.INSTANCE.client.player.getPos();

		// get the player's current dimension
		//String dim = formatDimType( Xenon.INSTANCE.client.world.getDimension() );

		// round and display
		String loc = String.format(
				"[%d %d %d](%s)",
				Math.round( currentPos.getX() ),
				Math.round( currentPos.getY() ),
				Math.round( currentPos.getZ() ),
				""
		);

		Xenon.INSTANCE.client.player.sendChatMessage( loc );
	}
}