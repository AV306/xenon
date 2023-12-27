package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.ActionResult;

public interface MobEntityRendererEvents
{
	Event<RenderLabelTextEvent> RENDER_LABEL_TEXT = EventFactory.createArrayBacked(
			RenderLabelTextEvent.class,
			(listeners) -> (entity) ->
			{
				for ( RenderLabelTextEvent listener : listeners )
				{
					ActionResult result = listener.onGetHasLabel( entity );

					if ( result != ActionResult.PASS ) return result;
				}

				return ActionResult.PASS;
			}
	);

	@FunctionalInterface
	interface RenderLabelTextEvent<T extends MobEntity>
	{
		ActionResult onGetHasLabel( T entity );
	}
}
