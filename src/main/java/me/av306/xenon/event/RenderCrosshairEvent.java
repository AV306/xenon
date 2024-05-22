package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.ActionResult;

public class RenderCrosshairEvent
{
    public static final Event<StartRender> START_RENDER = EventFactory.createArrayBacked(
            StartRender.class,
            (listeners) -> (drawContext, tickDelta) ->
            {
                for ( StartRender listener : listeners )
                {
                    ActionResult result = listener.onStartRenderCrosshair( drawContext, tickDelta );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    public static final Event<EndRender> END_RENDER = EventFactory.createArrayBacked(
            EndRender.class,
            (listeners) -> (drawContext, tickDelta) ->
            {
                for ( EndRender listener : listeners )
                {
                    ActionResult result = listener.onEndRenderCrosshair( drawContext, tickDelta );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    @FunctionalInterface
    public interface StartRender
    {
        ActionResult onStartRenderCrosshair( DrawContext drawContext, float tickDelta );
    }

    @FunctionalInterface
    public interface EndRender
    {
        ActionResult onEndRenderCrosshair( DrawContext drawContext, float tickDelta );
    }
}
