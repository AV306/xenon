package me.av306.xenon.mixin;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.MouseScrollEvent;
import me.av306.xenon.mixinterface.IMouse;
import net.minecraft.client.Mouse;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( Mouse.class )
public class MouseMixin implements IMouse
{
    @Shadow
    private double cursorDeltaX;

    @Shadow
    private double cursorDeltaY;

    @Override
    public void accelerateDeltaX( double x )
    {
        this.cursorDeltaX += x;
    }

    @Override
    public void accelerateDeltaY( double y )
    {
        this.cursorDeltaY += y;
    }


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
