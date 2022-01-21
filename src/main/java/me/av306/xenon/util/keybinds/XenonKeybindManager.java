package me.av306.xenon.util.keybinds;

import me.av306.xenon.features.interfaces.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class XenonKeybindManager
{

    public XenonKeybindManager() {}

    public void register( XenonKeybind kb )
    {
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
            // Holding ("Normal") feature
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
