package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface ScrollInHotbarEvent
{
    Event<ScrollInHotbarEvent> EVENT = EventFactory.createArrayBacked(
            ScrollInHotbarEvent.class,
            (listeners) -> (scrollAmount) ->
            {
                for ( ScrollInHotbarEvent listener : listeners )
                {
                    ActionResult result = listener.interact( scrollAmount);

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    ActionResult interact( double scrollAmount );
}
