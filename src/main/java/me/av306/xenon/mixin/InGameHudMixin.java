package me.av306.xenon.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.av306.xenon.Xenon;
import me.av306.xenon.config.GeneralConfigGroup;
import me.av306.xenon.event.RenderCrosshairEvent;
import me.av306.xenon.event.RenderInGameHudEvent;

import net.minecraft.client.gui.DrawContext;
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
            /*at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay(Lnet/minecraft/client/gui/DrawContext;)V", // render AFTER vignette without doing shit to the portal overlay
                    shift = At.Shift.AFTER
            ),*/
            at = @At( "TAIL" ),
            method = "render(Lnet/minecraft/client/gui/DrawContext;F)V",
            cancellable = true
    )
    private void onRenderAfterVignette( DrawContext context, float tickDelta, CallbackInfo ci )
    {
        ActionResult result = RenderInGameHudEvent.AFTER_VIGNETTE.invoker().onAfterVignette( context, tickDelta );

        // cancel if fail
        if ( result == ActionResult.FAIL )
            ci.cancel();
    }

    @Inject(
            /*at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;scaledHeight:I", // right after scaledHeight is set
                    opcode = Opcodes.PUTFIELD,
                    ordinal = 0
            ),*/
            at = @At( "HEAD" ),
            method = "render(Lnet/minecraft/client/gui/DrawContext;F)V",
            cancellable = true
    ) // InGameHudRenderer became a LOT more complicated, I have no clue where to inject this and it doesn't seem to be used anywhere :/
    private void onStartRender( DrawContext drawContext, float tickDelta, CallbackInfo ci )
    {
        ActionResult result = RenderInGameHudEvent.START.invoker()
                .onStartRender( drawContext, tickDelta );

        // cancel if fail
        if ( result == ActionResult.FAIL )
            ci.cancel();
    }

    /*@Inject( // This is for the debug crosshair
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;renderCrosshair(I)V",
                    shift = At.Shift.BEFORE
            ),
            method = "renderCrosshair(Lnet/minecraft/client/util/math/MatrixStack;)V",
            cancellable = true
    )*/

    @Inject(
            at = @At( "HEAD" ),
            method = "renderCrosshair(Lnet/minecraft/client/gui/DrawContext;F)V",
            cancellable = true
    )
    private void onRenderCrosshair( DrawContext context, float tickDelta, CallbackInfo ci )
    {
        if ( RenderCrosshairEvent.START_RENDER.invoker()
                .onStartRenderCrosshair( context, tickDelta ) == ActionResult.FAIL )
            ci.cancel();
    }

    @Inject(
            at = @At( "TAIL" ),
            method = "renderCrosshair(Lnet/minecraft/client/gui/DrawContext;F)V",
            cancellable = true
    )
    private void onEndRenderCrosshair( DrawContext context, float tickDelta, CallbackInfo ci )
    {

        if ( RenderCrosshairEvent.END_RENDER.invoker()
                .onEndRenderCrosshair( context, tickDelta ) == ActionResult.FAIL )
            ci.cancel();
    }
}