package me.av306.xenon.features.render;

import me.av306.xenon.feature.IToggleableFeature;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

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
	}

	@Override
	protected void onEnable()
	{

	}

	@Override
	protected void onDisable()
	{

	}

	// Used in `me.av306.xenon.mixin.EntityRendererMixin`
	// Breaking conventions a bit since these mixins are used solely for this feature
	public Text modifyEntityLabelText( Entity entity, Text text )
	{
		if ( !this.isEnabled ) return text;

		try
        {
            LivingEntity livingEntity = (LivingEntity) entity;

            text = text.copy().append( String.format( " §c(%f♥)§r", livingEntity.getHealth() ) );
        }
        catch ( ClassCastException ignored ) {}

        return text;
	}

	// Used in `me.av306.xenon.mixin.MobEntityRendererMixin`
	public boolean shouldForceEntityNameplate()
	{
		return this.isEnabled;
	}
}
