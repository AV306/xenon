package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

public class GameRenderEvents
{
	public static final Event<RenderWorld> RENDER_WORLD = EventFactory.createArrayBacked(
			RenderWorld.class,
			(listeners) -> (tickDelta, limitTime, matrices) ->
			{
				for ( RenderWorld listener : listeners )
				{
					ActionResult result = listener.onRenderWorld( tickDelta, limitTime, matrices );

					if ( result != ActionResult.PASS ) return result;
				}
				return ActionResult.PASS;
			}
	);

	@FunctionalInterface
	public interface RenderWorld
	{
		ActionResult onRenderWorld( float tickDelta, long limitTime, MatrixStack matrices );
	}
}
