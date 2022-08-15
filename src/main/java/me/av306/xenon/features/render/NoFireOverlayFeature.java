package me.av306.xenon.features.render;

import me.av306.xenon.event.RenderFireOverlayEvent;
import me.av306.xenon.feature.IToggleableFeature;

import net.minecraft.util.ActionResult;

public class NoFireOverlayFeature extends IToggleableFeature
{
    public NoFireOverlayFeature()
	{
		// set name
		super( "NoFireOverlay" );


		// register listeners
		RenderFireOverlayEvent.EVENT.register(
			(client, matrices) ->
			{
				if ( this.isEnabled ) return ActionResult.FAIL; // cancel if enabled >:D
				else return ActionResult.PASS;
			}
		);
	}
	
	
	// battle-test events
    @Override
    protected void onEnable()
    {
			
    }

	
    @Override
    protected void onDisable()
    {
			
    }
}
