package me.av306.xenon.mixins;

import me.av306.xenon.Xenon;

import me.av306.xenon.features.NoFireOverlayFeature;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin( InGameOverlayRenderer.class )
public class InGameOverlayRendererMixin
{
    @Inject(
            at = {@At("HEAD")},
            method = {
                    "renderFireOverlay(Lnet/minecraft/client/MinecraftClient; Lnet/minecraft/client/util/math/MatrixStack;)V"
            },
            cancellable = true
    )
    private static void onRenderFireOverlay( MinecraftClient client, MatrixStack matrixStack, CallbackInfo ci )
    {
        Xenon.INSTANCE.LOGGER.info( "u on fire" );
        if ( NoFireOverlayFeature.isEnabled ) ci.cancel();
    }
}