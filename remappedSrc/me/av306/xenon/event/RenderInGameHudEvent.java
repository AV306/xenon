package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

public class RenderInGameHudEvent
{
    public static final Event<StartRender> START = EventFactory.createArrayBacked(
            StartRender.class,
            (listeners) -> (matrices, tickDelta) ->
            {
                for ( StartRender listener : listeners )
                {
                    ActionResult result = listener.onStartRender( matrices, tickDelta );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    public static final Event<AfterVignette> AFTER_VIGNETTE = EventFactory.createArrayBacked(
            AfterVignette.class,
            (listeners) -> (matrices, tickDelta) ->
            {
                for ( AfterVignette listener : listeners )
                {
                    ActionResult result = listener.onAfterVignette( matrices, tickDelta );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    @FunctionalInterface
    public interface StartRender
    {
        ActionResult onStartRender( MatrixStack matrices, float tickDelta );
    }

    @FunctionalInterface
    public interface AfterVignette
    {
        ActionResult onAfterVignette( MatrixStack matrixes, float tickDelta );
    }
}
