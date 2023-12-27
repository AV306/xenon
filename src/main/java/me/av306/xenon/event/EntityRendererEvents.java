package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public interface EntityRendererEvents
{
	Event<GetLabelText> RENDER_LABEL_TEXT = EventFactory.createArrayBacked(
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

	@FunctionalInterface
	interface GetLabelText<T extends Entity>
	{
		ActionResult onGetLabelText( T entity, Text text );
	}

	class EventData
	{
		public static Text LABEL_TEXT_OVERRIDE;
		public static boolean SHOULD_OVERRIDE_LABEL_TEXT = false;
	}
}
