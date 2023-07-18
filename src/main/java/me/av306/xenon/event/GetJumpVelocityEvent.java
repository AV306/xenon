package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface GetJumpVelocityEvent
{
    Event<GetJumpVelocityEvent> EVENT = EventFactory.createArrayBacked(
            GetJumpVelocityEvent.class,
            (listeners) -> () ->
            {
                for ( GetJumpVelocityEvent listener : listeners )
                {
                    ActionResult result = listener.interact();

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    ActionResult interact();
}
