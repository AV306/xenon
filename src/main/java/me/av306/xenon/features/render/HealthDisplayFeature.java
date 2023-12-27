package me.av306.xenon.features.render;

import me.av306.xenon.event.EntityRendererEvents;
import me.av306.xenon.event.MobEntityRendererEvents;
import me.av306.xenon.feature.IToggleableFeature;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

// See `me.av306.xenon.mixin.EntityRendererMixin`.
// Breaking conventions here because I don't feel like making a new event type.
// FIXME: Make this adhere to my convention that feature-specific code should not be in mixins
public class HealthDisplayFeature extends IToggleableFeature
{
	private static HealthDisplayFeature INSTANCE;
	public static HealthDisplayFeature getInstance() { return INSTANCE; }
	public HealthDisplayFeature()
	{
		super( "HealthDisplay", "hd", "healthdisp" );

		//	HudRenderCallback.EVENT.register( this::onHudRender );
		INSTANCE = this;

		MobEntityRendererEvents.RENDER_LABEL_TEXT.register( this::shouldForceEntityNameplate );

		EntityRendererEvents.RENDER_LABEL_TEXT.register( this::getLabelText );
	}

	private ActionResult getLabelText( Entity entity, Text text )
	{
		if ( !this.isEnabled ) return ActionResult.PASS;

		try
		{
			LivingEntity livingEntity = (LivingEntity) entity;

			EntityRendererEvents.EventData.LABEL_TEXT_OVERRIDE = text.copy().append( String.format( " §c(%.2f❤)§r", livingEntity.getHealth() ) );
		}
		catch ( ClassCastException ignored ) {}

		return ActionResult.PASS;
	}

	private ActionResult shouldForceEntityNameplate( MobEntity mobEntity )
	{
		return this.isEnabled ? ActionResult.FAIL : ActionResult.PASS;
	}

	@Override
	protected void onEnable()
	{
		EntityRendererEvents.EventData.SHOULD_OVERRIDE_LABEL_TEXT = true;
	}

	@Override
	protected void onDisable()
	{
		EntityRendererEvents.EventData.SHOULD_OVERRIDE_LABEL_TEXT = false;
	}
}
