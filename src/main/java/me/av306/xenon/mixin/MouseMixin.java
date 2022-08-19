package me.av306.xenon.mixin;

import me.av306.xenon.event.MouseScrollEvent;
import net.minecraft.client.Mouse;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( Mouse.class )
public class MouseMixin
{
    @Inject(
            at = @At( "RETURN" ),
            method = "onMouseScroll(JDD)V",
            cancellable = true
    )
    private void onOnMouseScroll( long window, double horizontal, double vertical, CallbackInfo ci )
    {
        ActionResult result = MouseScrollEvent.EVENT.invoker().interact( window, horizontal, vertical );

        if ( result == ActionResult.FAIL ) ci.cancel();
    }
}