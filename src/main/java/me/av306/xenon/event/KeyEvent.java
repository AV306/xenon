package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface KeyEvent
{
    Event<KeyEvent> EVENT = EventFactory.createArrayBacked(
            KeyEvent.class,
            (listeners) -> (window, key, scancode, action, modifiers) ->
            {
                for ( KeyEvent listener : listeners )
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
