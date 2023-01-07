package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface BeginRenderTickEvent
{
    Event<BeginRenderTickEvent> EVENT = EventFactory.createArrayBacked(
            BeginRenderTickEvent.class,
            (listeners) -> (timeMillis) ->
            {
                for ( BeginRenderTickEvent listener : listeners )
                {
                    ActionResult result = listener.interact( timeMillis );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    ActionResult interact( long timeMillis );
}
