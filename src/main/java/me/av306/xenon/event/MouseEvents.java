package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface MouseEvents
{
   /*Event<MouseButtonEvent> ON_MOUSE_BUTTON = EventFactory.createArrayBacked(
            MouseButtonEvent.class,
            (listeners) -> (window, button, action, mods) ->
            {
                for ( MouseButtonEvent listener : listeners )
                {
                    ActionResult result = listener.onMouseButton( window, button, action, mods );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );*/

    Event<MouseScrollEvent> ON_MOUSE_SCROLL = EventFactory.createArrayBacked(
            MouseScrollEvent.class,
            (listeners) -> (window, horizontal, vertical) ->
            {
                for ( MouseScrollEvent listener : listeners )
                {
                    ActionResult result = listener.onMouseScroll( window, horizontal, vertical );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );


    @FunctionalInterface
    interface MouseButtonEvent
    {
        ActionResult onMouseButton( long window, int button, int action, int mods );
    }

    @FunctionalInterface
    interface MouseScrollEvent
    {
        ActionResult onMouseScroll( long window, double horizontal, double vertical );
    }
}