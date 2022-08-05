package me.av306.xenon.mixin;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.EventFields;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( GameRenderer.class )
public class GameRendererMixin implements AutoCloseable, SynchronousResourceReloader
{
    @Inject(
            at = @At( value = "RETURN", ordinal = 1 ),
            method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D",
            cancellable = true)
    private void onGetFov( Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir )
    {
        double originalFov = Xenon.INSTANCE.client.options.getFov().getValue();
        cir.setReturnValue( originalFov + EventFields.FOV_MODIFIER );
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
