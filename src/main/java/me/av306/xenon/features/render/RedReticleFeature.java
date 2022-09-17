package me.av306.xenon.features.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.RedReticleGroup;
import me.av306.xenon.event.RenderCrosshairEvent;
import me.av306.xenon.feature.IToggleableFeature;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class RedReticleFeature extends IToggleableFeature
{
    public RedReticleFeature()
    {
        super( "Red Reticle", "aimassist", "rr" );
        RenderCrosshairEvent.START_RENDER.register( this::onStartRenderCrosshair );
        RenderCrosshairEvent.END_RENDER.register( this::onEndRenderCrosshair );
    }

    private ActionResult onStartRenderCrosshair( MatrixStack matrixStack )
    {
        if ( this.isEnabled )
        {
            if ( RedReticleGroup.disableBlend ) RenderSystem.disableBlend();

            HitResult hitResult = Xenon.INSTANCE.client.crosshairTarget;

            assert hitResult != null;
            if ( hitResult.getType() == HitResult.Type.ENTITY )
            {
                Entity entity = ((EntityHitResult) hitResult).getEntity();

                if ( entity.isTeammate( Xenon.INSTANCE.client.player ) )
                {
                    // Friendly!
                    // Green crosshair
                    RenderSystem.setShaderColor( 0f, 1f, 0f, 1f );
                } else if ( entity instanceof HostileEntity )
                {
                    // Hostile and enemy!
                    // Red crosshair
                    RenderSystem.setShaderColor( 1f, 0f, 0f, 1f );
                }
            }
        }

        return ActionResult.PASS;
    }

    private ActionResult onEndRenderCrosshair(MatrixStack matrixStack)
    {
        if ( this.isEnabled ) RenderSystem.enableBlend();
        return ActionResult.PASS;
    }

    @Override
    protected void onEnable() {}

    @Override
    protected void onDisable() {}
}
