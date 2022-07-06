package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface ClientPlayerTickEvents
{
    Event<StartPlayerTick> START_PLAYER_TICK = EventFactory.createArrayBacked(
            StartPlayerTick.class,
            (listeners) -> () ->
            {
                for ( StartPlayerTick listener : listeners )
                {
                    ActionResult result = listener.onStartPlayerTick();

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    Event<EndPlayerTick> END_PLAYER_TICK = EventFactory.createArrayBacked(
            EndPlayerTick.class,
            (listeners) -> () ->
            {
                for ( EndPlayerTick listener : listeners )
                {
                    ActionResult result = listener.onEndPlayerTick();

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    @FunctionalInterface
    interface StartPlayerTick
    {
        ActionResult onStartPlayerTick();
    }

    @FunctionalInterface

    interface EndPlayerTick
    {
        ActionResult onEndPlayerTick();
    }
}