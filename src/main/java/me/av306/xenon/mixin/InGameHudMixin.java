package me.av306.xenon.mixin;

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
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay(Lnet/minecraft/client/util/math/MatrixStack;)V", // render AFTER vignette without doing shit to the portal overlay
                    shift = At.Shift.AFTER
            ),
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V",
            cancellable = true
    )
    private void onRender( MatrixStack matrices, float tickDelta, CallbackInfo ci )
    {
        ActionResult result = InGameHudRenderCallback.AFTER_VIGNETTE.invoker().interact( matrices, tickDelta );

        // cancel if fail
        if ( result == ActionResult.FAIL )
            ci.cancel();
    }
}