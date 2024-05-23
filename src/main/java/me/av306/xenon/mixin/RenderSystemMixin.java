package me.av306.xenon.mixin;

import me.av306.xenon.config.GeneralConfigGroup;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin( RenderSystem.class )
public class RenderSystemMixin
{
    @ModifyVariable(
            at = @At( "HEAD" ),
            ordinal = 0,
            method = "renderCrosshair(I)V",
            argsOnly = true
    ) // Target the very first local variable - the crosshair size
    private static int onRenderDebugCrosshair( int size )
    {
        // This method name is a bit misleading
        return GeneralConfigGroup.debugCrosshairSize;
    }
}