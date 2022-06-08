package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface GetReachDistanceEvent
{
    Event<GetReachDistanceEvent> EVENT = EventFactory.createArrayBacked(
            GetReachDistanceEvent.class,
            (listeners) -> () ->
            {
                for ( GetReachDistanceEvent listener : listeners )
                {
                    ActionResult result = listener.interact();

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    ActionResult interact();
}
