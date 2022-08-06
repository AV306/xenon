package me.av306.xenon.mixin;

import me.av306.xenon.event.EventFields;
import me.av306.xenon.event.GetFovEvent;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( GameRenderer.class )
public class GameRendererMixin implements AutoCloseable, SynchronousResourceReloader
{
    @Shadow @Final private Camera camera;

    @Inject(
            at = @At( value = "RETURN", ordinal = 1 ),
            method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D",
            cancellable = true
    )
    private void onGetFov( Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir )
    {
        GetFovEvent.EVENT.invoker().interact( camera, tickDelta, changingFov );
        if ( EventFields.shouldOverrideFov ) cir.setReturnValue( EventFields.FOV_OVERRIDE );
        else cir.setReturnValue( cir.getReturnValue() + EventFields.FOV_MODIFIER );
    }

    @Shadow
    @Override
    public void close() throws Exception
    {

    }

    @Shadow
    @Override
    public void reload( ResourceManager manager )
    {

    }
}
