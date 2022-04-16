package me.av306.xenon.event.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public interface ChatHudAddMessageCallback
{
    Event<ChatHudAddMessageCallback> EVENT = EventFactory.createArrayBacked(
            ChatHudAddMessageCallback.class,
            (listeners) -> (message) ->
            {
                for ( ChatHudAddMessageCallback listener : listeners )
                {
                    ActionResult result = listener.interact( message );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    ActionResult interact( Text message );
}
