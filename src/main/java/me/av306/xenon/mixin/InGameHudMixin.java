package me.av306.xenon.mixin;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.callback.InGameHudRenderCallback;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin( InGameHud.class )
public class InGameHudMixin
{
    @Inject(
            at = @At("HEAD"),
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V",
            cancellable = true
    )
    private void onRender( MatrixStack matrices, float tickDelta, CallbackInfo ci )
    {
        ActionResult result = InGameHudRenderCallback.EVENT.invoker().interact( matrices, tickDelta );

        // cancel if fail
        if ( result == ActionResult.FAIL )
            ci.cancel();
    }
}