package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.Camera;
import net.minecraft.util.ActionResult;

public interface GetFovEvent
{
    Event<GetFovEvent> EVENT = EventFactory.createArrayBacked(
            GetFovEvent.class,
            (listeners) -> (camera, tickDelta, changingFov) ->
            {
                for ( GetFovEvent listener : listeners )
                {
                    ActionResult result = listener.interact( camera, tickDelta, changingFov );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    ActionResult interact( Camera camera, float tickDelta, boolean changingFov );

    public static class EventData
    {
        public static float FOV_ZOOM_LEVEL = 1f;
    }
}
