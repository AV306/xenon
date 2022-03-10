package me.av306.xenon.event.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

/**
 * Callback for InGameOverlayRenderer.
 * Triggered after the method(s) is/are invoked, before any function calls (i.e. HEAD).
 *
 * Upon return:
 * - SUCCESS: Cancel further processing and render overlays as normal.
 * - PASS: Continue to next listener and 
 *	 default to SUCCESS when there are no other listeners.
 * - FAIL: Cancel the event.
 */
public class InGameOverlayRendererRenderFireOverlayCallback
{		
	public static final Event<InGameOverlayRendererRenderFireOverlayCallback> EVENT = EventFactory.createArrayBacked(
		RenderFireOverlayCallback.class,
		(listeners) -> (client, matrices) ->
		{
			// iterate over each listener
			for ( RenderFireOverlayCallback listener : listeners )
			{
				// let the listener interact with the event
				ActionResult result = listener.interact( client, matrices );
				// stop and return if the listener chooses not to pass it on
				if ( result != ActionResult.PASS )
					return result;
			}

			return ActionResult.PASS;
		}
	);

	ActionResult interact( MinecraftClient client, MatrixStack matrices );
}
