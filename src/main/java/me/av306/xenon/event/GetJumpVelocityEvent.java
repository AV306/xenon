package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class GetJumpVelocityEvent
{
    Event<GetJumpVelocityEvent> EVENT = EventFactory.createArrayBacked(
           GetJumpVelocityEvent.class,

    );
}
