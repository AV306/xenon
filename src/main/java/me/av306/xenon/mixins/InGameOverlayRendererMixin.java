package me.av306.xenon.mixins;

import me.av306.xenon.Xenon;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin( InGameOverlayRendererMixin.class )
public class InGameOverlayRendererMixin
{
    @Inject(
            at = @At("HEAD"),
            method = {
            "renderOverlays(Lnet/minecraft/client/MinecraftClient; Lnet/minecraft/client/util/math/MatrixStack;)V"
            }
    )
    private static void onRenderOverlays( MinecraftClient client, MatrixStack matrixStack, CallbackInfo ci )
    {
        Xenon.INSTANCE.LOGGER.info("This line is printed by Xenon's game renderer mixin!");
    }
}