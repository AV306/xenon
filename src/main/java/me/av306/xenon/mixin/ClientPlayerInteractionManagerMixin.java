package me.av306.xenon.mixin;

import me.av306.xenon.Xenon;
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
    @Inject( at =
            @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;getId()I",
                    ordinal = 0
            ),
            method = "updateBlockBreakingProgress(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z"
    )
    private void onPlayerDamageBlock( BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir )
    {
        Xenon.INSTANCE.LOGGER.info( "triggered" );
        PlayerDamageBlockEvent.EVENT.invoker().interact( pos, direction );
    }
}
