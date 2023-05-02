package me.av306.xenon.mixin;

import me.av306.xenon.event.BeginRenderTickEvent;
import net.minecraft.client.render.RenderTickCounter;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( RenderTickCounter.class )
public abstract class RenderTickCounterMixin
{
    @Shadow
    private float lastFrameDuration;

    @Inject(
            at =  @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/RenderTickCounter;prevTimeMillis:J",
                    opcode = Opcodes.PUTFIELD,
                    ordinal = 0
            ),
            method = "beginRenderTick(J)I"
    ) // inject where `beginRenderTick()` sets `prevTimeMillis`
    public void onBeginRenderTick( long timeMillis, CallbackInfoReturnable<Integer> cir )
    {
        //lastFrameDuration *= TimerGroup.timerSpeed; // this works, no fucking idea why
        BeginRenderTickEvent.EVENT.invoker().interact( timeMillis );
    }

    /*@Override
    public float getLastFrameDuration()
    {
        return lastFrameDuration;
    }

    @Override
    public void setLastFrameDuration( float timeMillis )
    {
        lastFrameDuration = timeMillis;
    }*/
}
