package me.av306.xenon.mixin;

import me.av306.xenon.config.GeneralConfigGroup;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin( RenderSystem.class )
public class RenderSystemMixin
{
    // This is dark magic,
    // but it seems to work.
    // Captures the variable passed to renderCrosshair()
    // that sets the size of the crosshair,
    // and overrides it.
    // It is safe to override
    // (unlike FOV, which has an option to either override or modify)
    // because the crosshair size is hardcoded to 10.
    @ModifyVariable(
            at = @At( "HEAD" ), // Idk what this does for now
            ordinal = 0, // There's only one argument - size
            method = "renderCrosshair(I)V",
            argsOnly = true
    )
    private static int onRenderSystemRenderCrosshair( int size )
    {
        //return EventFields.CROSSHAIR_SIZE_OVERRIDE;
        return GeneralConfigGroup.customCrosshairSize;
    }
}