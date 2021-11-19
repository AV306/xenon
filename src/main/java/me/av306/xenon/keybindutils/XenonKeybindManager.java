package me.av306.xenon.keybindutils;

import me.av306.xenon.Xenon;
import me.av306.xenon.features.FullBrightFeature;
import me.av306.xenon.features.TestUpdatableFeature;
import me.av306.xenon.features.interfaces.IFeature;
import me.av306.xenon.features.interfaces.IToggleableFeature;
import me.av306.xenon.features.interfaces.IUpdatableFeature;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.List;

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
        else
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
    }
}
