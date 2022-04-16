package me.av306.xenon.keybind;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class XenonKeybindManager
{	
		// TODO: should we configure feature attribs by instance fields or by static fields?
    public XenonKeybindManager() {}
	
    public void register( XenonKeybind kb )
    {
			//kb.feature.init(); // initialise the keybind's Feature. EDIT: now in constructor
			
        if ( kb.feature instanceof IToggleableFeature )
        {
            // Toggling feature
            ClientTickEvents.END_CLIENT_TICK.register( (client) ->
                    {
                        if ( kb.keybind.wasPressed() )
                            ((IToggleableFeature) kb.feature).toggle();
                    }
            );
        }
        /*else if ( kb.feature instanceof IContinuousFeature )
        {
            // Holding ("Normal") feature e.g. zoom
            ClientTickEvents.END_CLIENT_TICK.register( (client) ->
                    {
                        if ( kb.keybind.isPressed() )
                            kb.feature.enable();
                    }
            );
        }*/
        else
        {
			// IFeature, fire-and-forget
            ClientTickEvents.END_CLIENT_TICK.register( (client) ->
                    {
                        if ( kb.keybind.wasPressed() )
                        {
                            kb.feature.enable();
                            Xenon.INSTANCE.LOGGER.info( "IFeature triggered" );
                        }
                    }
            );
        }
    }
}
