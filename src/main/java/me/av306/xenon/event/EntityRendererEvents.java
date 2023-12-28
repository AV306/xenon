package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public interface EntityRendererEvents
{
	Event<GetLabelText> GET_LABEL_TEXT = EventFactory.createArrayBacked(
			GetLabelText.class,
			(listeners) -> (entity, text) ->
			{
				for ( GetLabelText listener : listeners )
				{
					ActionResult result = listener.onGetLabelText( entity, text );

					if ( result != ActionResult.PASS ) return result;
				}

				return ActionResult.PASS;
			}
	);

	Event<GetHasLabel> GET_HAS_LABEL = EventFactory.createArrayBacked(
			GetHasLabel.class,
			(listeners) -> (entity) ->
			{
				for ( GetHasLabel listener : listeners )
				{
					ActionResult result = listener.onGetHasLabel( entity );

					if ( result != ActionResult.PASS ) return result;
				}

				return ActionResult.PASS;
			}
	);

	@FunctionalInterface
	interface GetLabelText<T extends Entity>
	{
		ActionResult onGetLabelText( T entity, Text text );
	}

	@FunctionalInterface
	interface GetHasLabel<T extends Entity>
	{
		ActionResult onGetHasLabel( T entity );
	}

	class EventData
	{
		public static Text LABEL_TEXT_OVERRIDE = null;
		public static boolean SHOULD_OVERRIDE_LABEL_TEXT = false;
	}
}
