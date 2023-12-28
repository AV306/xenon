package me.av306.xenon.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.ActionResult;

public interface MobEntityRendererEvents
{
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
	interface GetHasLabel<T extends MobEntity>
	{
		ActionResult onGetHasLabel( T entity );
	}
}
