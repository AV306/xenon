package me.av306.xenon.mixin;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.EventFields;
import me.av306.xenon.event.GameRenderEvents;
import me.av306.xenon.event.GetFovEvent;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.ActionResult;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( GameRenderer.class )
public abstract class GameRendererMixin implements AutoCloseable, SynchronousResourceReloader
{
    @Shadow
    public abstract void reset();

    // FIXME: replace with mixinextras ModifyReturnValue
    @Inject(
            at = @At( value = "RETURN", ordinal = 1 ),
            method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D",
            cancellable = true
    )

    /*
    @ModifyReturnValue(
            at = @At( value = "RETURN", ordinal = 1 ),
            method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D"
    )
    */
    private void onGetFov( Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir )
    {
        // Call event
        GetFovEvent.EVENT.invoker().interact( camera, tickDelta, changingFov );
        
        //if ( EventFields.shouldOverrideFov ) cir.setReturnValue( EventFields.FOV_OVERRIDE / EventFields.FOV_ZOOM_LEVEL );
        //else cir.setReturnValue( (cir.getReturnValue() + EventFields.FOV_MODIFIER) / EventFields.FOV_ZOOM_LEVEL );

        // I have never used the other two things I added (modifier and override)
        // so out they go
        cir.setReturnValue( cir.getReturnValue() / GetFovEvent.EventData.FOV_ZOOM_LEVEL );

        //Xenon.INSTANCE.sendInfoMessage( String.valueOf( cir.getReturnValue() ) );
    }

   @Inject(
           at = @At(
                   value = "FIELD",
                   target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
                   opcode = Opcodes.GETFIELD,
                   ordinal = 0
           ),
           method = "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V",
           cancellable = true
   )
    private void onRenderWorld( float tickDelta, long finishTimeNanos, MatrixStack matrices, CallbackInfo ci )
    {
        ActionResult result = GameRenderEvents.RENDER_WORLD.invoker()
                .onRenderWorld( tickDelta, finishTimeNanos, matrices );

        if ( result == ActionResult.FAIL ) ci.cancel();
    }
}
