package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.Nullable;

public interface LivingEntityKilledEvent
{
    Event<LivingEntityKilledEvent> EVENT = EventFactory.createArrayBacked(
            LivingEntityKilledEvent.class,
            (listeners) -> (killer) ->
            {
                for ( LivingEntityKilledEvent listener : listeners )
                {
                    ActionResult result = listener.interact( killer );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    ActionResult interact( @Nullable LivingEntity killer );
}
