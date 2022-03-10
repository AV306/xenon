package me.av306.xenon.features;

import me.av306.xenon.event.callback.InGameOverlayRendererRenderFireOverlayCallback;
import me.av306.xenon.feature.IToggleableFeature;

import net.minecraft.util.ActionResult;

public class NoFireOverlayFeature extends IToggleableFeature
{
    public NoFireOverlayFeature()
		{ 
			// set name
			super( "NoFireOverlay" );
			
			// register listeners
			InGameOverlayRendererRenderFireOverlayCallback.EVENT.register(
				(client, matrices) -> 
				{
					if ( this.isEnabled ) return ActionResult.FAIL; // cancel if enabled >:D
					else return ActionResult.PASS;
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
			
    }
}
