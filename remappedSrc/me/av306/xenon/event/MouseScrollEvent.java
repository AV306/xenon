package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface MouseScrollEvent
{
    Event<MouseScrollEvent> EVENT = EventFactory.createArrayBacked(
            MouseScrollEvent.class,
            (listeners) -> (window, horizontal, vertical) ->
            {
                for ( MouseScrollEvent listener : listeners )
                {
                    ActionResult result = listener.interact( window, horizontal, vertical );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    ActionResult interact( long window, double horizontal, double vertical );
}
