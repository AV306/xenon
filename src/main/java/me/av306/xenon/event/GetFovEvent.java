package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.Camera;
import net.minecraft.util.ActionResult;

public abstract class GetFovEvent
{
    public static final Event<GetFovEvent> EVENT = EventFactory.createArrayBacked(
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

    public static boolean SHOULD_OVERRIDE_FOV = false;
    public static double FOV_OVERRIDE = 90d;
    public static double FOV_MODIFIER = 0d;
    public static double FOV_MULTIPLIER = 1d;

    ActionResult interact( Camera camera, float tickDelta, boolean changingFov );
}
