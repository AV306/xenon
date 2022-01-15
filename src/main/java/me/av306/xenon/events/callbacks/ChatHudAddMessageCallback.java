package me.av306.xenon.events.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

/**
 * Callback for adding a message to chat.
 * Called before a message is displayed.
 * Upon return:
 * - SUCCESS: Not a command, continue normally
 * - PASS: command, proceed to further processing.
 * - FAIL: something went wrong, don't do anything.
 */
public interface ChatHudAddMessageCallback
{
	Event<ChatHudAddMessageCallback> EVENT = EventFactory.createArrayBacked( ChatHudAddMessageCallback.class,
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