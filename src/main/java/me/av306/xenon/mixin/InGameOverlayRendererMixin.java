package me.av306.xenon.mixin;

import me.av306.xenon.event.RenderFireOverlayEvent;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin( InGameOverlayRenderer.class )
public class InGameOverlayRendererMixin
{
    @Inject(
            at = @At("HEAD"),
            method = "renderFireOverlay(Lnet/minecraft/client/MinecraftClient; Lnet/minecraft/client/util/math/MatrixStack;)V",
            cancellable = true
    )
    private static void onRenderFireOverlay( MinecraftClient client, MatrixStack matrices, CallbackInfo ci )
    {
			ActionResult result = RenderFireOverlayEvent.EVENT.invoker().interact( client, matrices );

			// cancel if fail
			if ( result == ActionResult.FAIL )
				ci.cancel();
    }
}