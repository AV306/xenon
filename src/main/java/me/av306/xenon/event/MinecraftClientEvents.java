package me.av306.xenon.event;

import net.minecraft.client.world.ClientWorld;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface MinecraftClientEvents
{
    Event<JoinWorldEvent> JOIN_WORLD = EventFactory.createArrayBacked(
            JoinWorldEvent.class,
            (listeners) -> (world) ->
            {
                for ( JoinWorldEvent listener : listeners )
                {
                    ActionResult result = listener.onJoinWorld( world );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    Event<JoinWorldEvent> JOIN_WORLD_TAIL = EventFactory.createArrayBacked(
            JoinWorldEvent.class,
            (listeners) -> (world) ->
            {
                for ( JoinWorldEvent listener : listeners )
                {
                    ActionResult result = listener.onJoinWorld( world );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    Event<DisconnectEvent> DISCONNECT = EventFactory.createArrayBacked(
            DisconnectEvent.class,
            (listeners) -> () ->
            {
                for ( DisconnectEvent listener : listeners )
                {
                    ActionResult result = listener.onDisconnect();

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    @FunctionalInterface
    interface JoinWorldEvent
    {
        ActionResult onJoinWorld( ClientWorld world );
    }

    @FunctionalInterface

    interface DisconnectEvent
    {
        ActionResult onDisconnect();
    }
}