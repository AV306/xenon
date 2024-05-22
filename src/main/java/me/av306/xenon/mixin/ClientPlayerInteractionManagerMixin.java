package me.av306.xenon.mixin;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.EventFields;
import me.av306.xenon.event.GetReachDistanceEvent;
import me.av306.xenon.event.PlayerDamageBlockEvent;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( ClientPlayerInteractionManager.class )
public class ClientPlayerInteractionManagerMixin
{
    @Inject(
            at =
            @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;getId()I",
                    ordinal = 0
            ),
            method = "updateBlockBreakingProgress(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z"
    )
    private void onPlayerDamageBlock( BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir )
    {
        //Xenon.INSTANCE.LOGGER.info( "triggered" );
        PlayerDamageBlockEvent.EVENT.invoker().interact( pos, direction );
    }

    /*@Inject(
            at = @At( "HEAD" ),
            method = "getReachDistance()F",
            cancellable = true
    )
    private void onGetReachDistance( CallbackInfoReturnable<Float> cir )
    {
        GetReachDistanceEvent.EVENT.invoker().interact();

        ClientPlayerInteractionManagerAccessor cpima = (ClientPlayerInteractionManagerAccessor) Xenon.INSTANCE.client.interactionManager;
        float reach = cpima.getGameMode().isCreative() ? 5.0f : 4.5f; // default reach

        reach += EventFields.REACH_MODIFIER;

        cir.setReturnValue( reach );
    }*/

    /*@Inject(
            at = @At("HEAD"),
            method = "hasExtendedReach()Z",
            cancellable = true
    )
    private void hasExtendedReach( CallbackInfoReturnable<Boolean> cir )
    {
        if ( EventFields.REACH_MODIFIER != 0f )
            cir.setReturnValue( true );
    }*/
}
