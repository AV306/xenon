package me.av306.xenon.event.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

public interface InGameHudRenderCallback
{
    Event<InGameHudRenderCallback> EVENT = EventFactory.createArrayBacked(
            InGameHudRenderCallback.class,
            (listeners) -> (matrices, tickDelta) ->
            {
                for ( InGameHudRenderCallback listener : listeners )
                {
                    ActionResult result = listener.interact( matrices, tickDelta );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    ActionResult interact( MatrixStack matrices, float tickDelta );
}
