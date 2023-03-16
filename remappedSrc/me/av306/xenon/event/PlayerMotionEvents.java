package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.MovementType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

public interface PlayerMotionEvents
{
    Event<PreMotionEvent> BEFORE_MOTION_PACKET = EventFactory.createArrayBacked(
            PreMotionEvent.class,
            (listeners) -> () ->
            {
                for ( PreMotionEvent listener : listeners )
                {
                    ActionResult result = listener.onPreMotion();

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    Event<PlayerMoveEvent> PLAYER_MOVE = EventFactory.createArrayBacked(
            PlayerMoveEvent.class,
            (listeners) -> (movementType, movement) ->
            {
                for ( PlayerMoveEvent listener : listeners )
                {
                    ActionResult result = listener.onPlayerMove( movementType, movement );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    Event<PostMotionEvent> AFTER_MOTION_PACKET = EventFactory.createArrayBacked(
            PostMotionEvent.class,
            (listeners) -> () ->
            {
                for ( PostMotionEvent listener : listeners )
                {
                    ActionResult result = listener.onPostMotion();

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    @FunctionalInterface
    interface PreMotionEvent
    {
        ActionResult onPreMotion();
    }

    @FunctionalInterface
    interface PlayerMoveEvent
    {
        ActionResult onPlayerMove( MovementType movementType, Vec3d movement );
    }

    @FunctionalInterface
    interface PostMotionEvent
    {
        ActionResult onPostMotion();
    }
}