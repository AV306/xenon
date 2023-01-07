package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public interface PlayerDamageBlockEvent
{
    Event<PlayerDamageBlockEvent> EVENT = EventFactory.createArrayBacked(
            PlayerDamageBlockEvent.class,
            (listeners) -> (pos, direction) ->
            {
                for ( PlayerDamageBlockEvent listener : listeners )
                {
                    ActionResult result = listener.interact( pos, direction );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    ActionResult interact( BlockPos pos, Direction direction );
}
