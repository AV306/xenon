package me.av306.xenon.features.render;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.text.TextFactory;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
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
}
