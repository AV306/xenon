package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;

public interface PlayerKillEntityEvent
{
    Event<PlayerKillEntityEvent> EVENT = EventFactory.createArrayBacked(
            PlayerKillEntityEvent.class,
            (listeners) -> (world, otherEntity) ->
            {
                for ( PlayerKillEntityEvent listener : listeners )
                {
                    //if ( otherEntity.equals(Xenon.INSTANCE.client.player))
                    ActionResult result = listener.interact( world, otherEntity );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    ActionResult interact( ServerWorld world, LivingEntity otherEntity );
}
