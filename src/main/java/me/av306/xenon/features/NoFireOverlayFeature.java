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
			InGameOverlayRendererRenderOverlaysCallback.EVENT.register(
				(client, matrices) -> 
				{
					if ( this.isEnabled ) return ActionResult.FAIL; // cancel if enabled >:D
					else reurn ActionmResult.PASS;
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
