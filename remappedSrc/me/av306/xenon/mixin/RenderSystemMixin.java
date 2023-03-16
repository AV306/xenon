package me.av306.xenon.mixin;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.GeneralConfigGroup;
import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( RenderSystem.class )
public class RenderSystemMixin
{
    @ModifyVariable(
            at = @At( "HEAD" ), // Idk what this does for now
            ordinal = 0, // There's only one argument - size
            method = "renderCrosshair(I)V",
            argsOnly = true
    )
    private static int onRenderDebugCrosshair( int size )
    {
        // This method name is a bit misleading
        return GeneralConfigGroup.debugCrosshairSize;
    }
}