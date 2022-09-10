package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

public class RenderCrosshairEvent
{
    public static final Event<StartRender> START_RENDER = EventFactory.createArrayBacked(
            StartRender.class,
            (listeners) -> (matrixStack) ->
            {
                for ( StartRender listener : listeners )
                {
                    ActionResult result = listener.onStartRenderCrosshair( matrixStack );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    @FunctionalInterface
    public interface StartRender
    {
        ActionResult onStartRenderCrosshair( MatrixStack matrixStack );
    }
}
