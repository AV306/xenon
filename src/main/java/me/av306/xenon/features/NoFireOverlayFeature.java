package me.av306.xenon.features;

import me.av306.xenon.event.callback.InGameOverlayRendererRenderOverlaysCallback;
import me.av306.xenon.feature.IToggleableFeature;

import net.minecraft.util.ActionResult;

public class NoFireOverlayFeature extends IToggleableFeature
{
    public NoFireOverlayFeature()
		{ 
			// set name
			super( "NoFireOverlay" );
			
			// register listeners
			InGameRendererRenderOverlaysCallback.EVENT.register(
				(client, matrices) -> 
				{
					if ( this.isEnabled )
						return ActionResult.FAIL; // cancel if enabled >:D
				}
			);
		}
	
	
		// battle-test events
    @Override
    public void onEnable()
    {
			
    }

	
    @Override
    public void onDisable()
    {
			// de-register listener (how?)
			
    }
}
