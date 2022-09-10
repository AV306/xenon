package me.av306.xenon.features.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.av306.xenon.event.RenderCrosshairEvent;
import me.av306.xenon.feature.IToggleableFeature;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

public class AimAssistFeature extends IToggleableFeature
{
    public AimAssistFeature()
    {
        super( "AimAssist" );
        RenderCrosshairEvent.START_RENDER.register( this::onStartRenderCrosshair );
    }

    private ActionResult onStartRenderCrosshair( MatrixStack matrixStack )
    {
        RenderSystem.setShaderColor( 1f, 0f, 0f, 1f );
        return ActionResult.PASS;
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
