package me.av306.xenon.keybind;

import me.av306.xenon.feature.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class XenonKeybindManager
{		
    public XenonKeybindManager() {}
	
    public void register( XenonKeybind kb )
    {
			kb.feature.init(); // initialise the keybind's Feature.
			
        if ( kb.feature instanceof IUpdatableFeature )
        {					
            // Updating feature
            ClientTickEvents.END_CLIENT_TICK.register( (client) ->
                    {
                        if ( kb.keybind.wasPressed() )
                        {
                            ((IUpdatableFeature) kb.feature).toggle();
                        }

                        ((IUpdatableFeature) kb.feature).onUpdate();
                    }
            );
        }
        else if ( kb.feature instanceof IToggleableFeature )
        {
            // Toggling feature
            ClientTickEvents.END_CLIENT_TICK.register( (client) ->
                    {
                        if ( kb.keybind.wasPressed() )
                        {
                            ((IToggleableFeature) kb.feature).toggle();
                        }
                    }
            );
        }
        else if ( kb.feature instanceof IContinuousFeature)
        {
            // Holding ("Normal") feature e.g. zoom
            ClientTickEvents.END_CLIENT_TICK.register( (client) ->
                    {
                        if ( kb.keybind.isPressed() )
                        {
                            kb.feature.onEnable();
                        }
                    }
            );
        }
        else
        {
						// one-off
            ClientTickEvents.END_CLIENT_TICK.register( (client) ->
                    {
                        if ( kb.keybind.wasPressed() )
                        {
                            kb.feature.onEnable();
                        }
                    }
            );
        }
    }
}
