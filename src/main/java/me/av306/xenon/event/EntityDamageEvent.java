package me.av306.xenon.event;

import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface EntityDamageEvent
{
    Event<EntityDamageEvent> EVENT = EventFactory.createArrayBacked(
            EntityDamageEvent.class,
            (listeners) -> (packet) ->
            {
                for ( EntityDamageEvent listener : listeners )
                {
                    ActionResult result = listener.interact( packet );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    ActionResult interact( EntityDamageS2CPacket packet );
}
