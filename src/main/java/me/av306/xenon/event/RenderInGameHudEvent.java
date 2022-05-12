package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

public interface RenderInGameHudEvent
{
    Event<RenderInGameHudEvent> HEAD = EventFactory.createArrayBacked(
            RenderInGameHudEvent.class,
            (listeners) -> (matrices, tickDelta) ->
            {
                for ( RenderInGameHudEvent listener : listeners )
                {
                    ActionResult result = listener.interact( matrices, tickDelta );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    Event<RenderInGameHudEvent> AFTER_VIGNETTE = EventFactory.createArrayBacked(
            RenderInGameHudEvent.class,
            (listeners) -> (matrices, tickDelta) ->
            {
                for ( RenderInGameHudEvent listener : listeners )
                {
                    ActionResult result = listener.interact( matrices, tickDelta );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    ActionResult interact( MatrixStack matrices, float tickDelta );
}
