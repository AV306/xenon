package me.av306.xenon.mixin;

import me.av306.xenon.event.MouseEvents;
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

    @Shadow
    private double x;
    @Shadow
    private double y;

    @Override
    public void accelerateDeltaX( double dx )
    {
        this.cursorDeltaX += dx;
    }

    @Override
    public void accelerateDeltaY( double dy )
    {
        this.cursorDeltaY += dy;
    }

    @Override
    public void changeX( double dx )
    {
        this.x += dx;
    }

    @Override
    public void changeY( double dy )
    {
        this.y += dy;
    }


    @Inject(
            at = @At( "RETURN" ),
            method = "onMouseScroll(JDD)V",
            cancellable = true
    )
    private void onOnMouseScroll( long window, double horizontal, double vertical, CallbackInfo ci )
    {
        ActionResult result = MouseEvents.ON_MOUSE_SCROLL.invoker().onMouseScroll( window, horizontal, vertical );

        if ( result == ActionResult.FAIL ) ci.cancel();
    }
}
