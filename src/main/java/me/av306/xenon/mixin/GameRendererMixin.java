package me.av306.xenon.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.av306.xenon.event.GameRenderEvents;
import me.av306.xenon.event.GetFovEvent;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.ActionResult;

import org.joml.Matrix4f;
import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( GameRenderer.class )
public abstract class GameRendererMixin implements AutoCloseable, SynchronousResourceReloader
{
    @Shadow
    public abstract void reset();

    @Inject(
            at = @At( value = "RETURN", ordinal = 1 ),
            method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D",
            cancellable = true
    )
    private void onGetFov( Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir )
    {
        // Call event
        GetFovEvent.EVENT.invoker().interact( camera, tickDelta, changingFov );
        
        //if ( EventFields.shouldOverrideFov ) cir.setReturnValue( EventFields.FOV_OVERRIDE / EventFields.FOV_ZOOM_LEVEL );
        //else cir.setReturnValue( (cir.getReturnValue() + EventFields.FOV_MODIFIER) / EventFields.FOV_ZOOM_LEVEL );

        cir.setReturnValue(
                (GetFovEvent.EventData.SHOULD_OVERRIDE_FOV
                        ? GetFovEvent.EventData.FOV_OVERRIDE
                        : cir.getReturnValue()) / GetFovEvent.EventData.FOV_ZOOM_LEVEL
        );

        //Xenon.INSTANCE.sendInfoMessage( String.valueOf( cir.getReturnValue() ) );
    }

    @ModifyReturnValue(
            at = @At( value = "RETURN", ordinal = 1 ),
            method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D"
    )
    public double modifyFov( double originalFov )
    {
        GetFovEvent.EVENT2.invoker().interact( originalFov );

        return GetFovEvent.EventData.SHOULD_OVERRIDE_FOV ?
                        GetFovEvent.EventData.FOV_OVERRIDE :
                        originalFov / GetFovEvent.EventData.FOV_ZOOM_LEVEL;
    }


    @Unique
    private MatrixStack matrices = new MatrixStack();

    @Inject(
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0
            ), // Inject at access of first local variable (TODO: document which one it is)
            method = "renderWorld(Lnet/minecraft/client/render/RenderTickCounter;)V",
            cancellable = true
    ) 
    private void onRenderWorld( RenderTickCounter tickCounter, CallbackInfo ci, @Local( ordinal = 1 ) Matrix4f matrix4f2 ) // we gotta steal the world matrix
    {
        matrices.push();
        matrices.multiplyPositionMatrix( matrix4f2 );

        ActionResult result = GameRenderEvents.RENDER_WORLD.invoker()
                .onRenderWorld( tickCounter.getTickDelta(), finishTimeNanos, matrices );

        if ( result == ActionResult.FAIL ) ci.cancel();
        matrices.pop();
    }
}
