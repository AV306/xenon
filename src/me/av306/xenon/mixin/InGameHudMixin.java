package me.av306.xenon.mixin;

import me.av306.xenon.event.RenderInGameHudEvent;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;
import org.objectweb.asm.Opcodes;
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
    private void onRenderAfterVignette( MatrixStack matrices, float tickDelta, CallbackInfo ci )
    {
        ActionResult result = RenderInGameHudEvent.AFTER_VIGNETTE.invoker().interact( matrices, tickDelta );

        // cancel if fail
        if ( result == ActionResult.FAIL )
            ci.cancel();
    }

    @Inject(
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;scaledHeight:I", // right after scaledHeight is set
                    opcode = Opcodes.PUTFIELD,
                    ordinal = 0
            ),
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V",
            cancellable = true
    )
    private void onStartRender( MatrixStack matrices, float tickDelta, CallbackInfo ci )
    {
        ActionResult result = RenderInGameHudEvent.HEAD.invoker().interact( matrices, tickDelta );

        // cancel if fail
        if ( result == ActionResult.FAIL )
            ci.cancel();
    }
}