package me.av306.xenon.event.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

public interface KeyboardKeyCallback
{
    Event<KeyboardKeyCallback> EVENT = EventFactory.createArrayBacked(
            KeyboardKeyCallback.class,
            (listeners) -> (window, key, scancode, action, modifiers) ->
            {
                for ( KeyboardKeyCallback listener : listeners )
                {
                    ActionResult result = listener.interact( window, key, scancode, action, modifiers );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    ActionResult interact( long window,
                           int key,
                           int scancode,
                           int action,
                           int modifiers );
}
