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

    public static final Event<EndRender> END_RENDER = EventFactory.createArrayBacked(
            EndRender.class,
            (listeners) -> (matrixStack) ->
            {
                for ( EndRender listener : listeners )
                {
                    ActionResult result = listener.onEndRenderCrosshair( matrixStack );

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

    @FunctionalInterface
    public interface EndRender
    {
        ActionResult onEndRenderCrosshair( MatrixStack matrixStack );
    }
}
