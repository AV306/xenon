package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public interface ChatHudAddMessageEvent
{
    Event<ChatHudAddMessageEvent> EVENT = EventFactory.createArrayBacked(
            ChatHudAddMessageEvent.class,
            (listeners) -> (message) ->
            {
                for ( ChatHudAddMessageEvent listener : listeners )
                {
                    ActionResult result = listener.interact( message );

                    if ( result != ActionResult.PASS ) return result;
                }

                return ActionResult.PASS;
            }
    );

    ActionResult interact( Text message );
}
