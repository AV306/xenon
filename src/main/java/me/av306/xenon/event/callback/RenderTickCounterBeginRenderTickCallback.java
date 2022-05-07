package me.av306.xenon.event.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

public interface RenderTickCounterBeginRenderTickCallback
{
    Event<RenderTickCounterBeginRenderTickCallback> EVENT = EventFactory.createArrayBacked(
            RenderTickCounterBeginRenderTickCallback.class,
            (listeners) -> (timeMillis) ->
            {
                for ( RenderTickCounterBeginRenderTickCallback listener : listeners )
                {
                    ActionResult result = listener.interact( timeMillis );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    ActionResult interact( long timeMillis );
}
