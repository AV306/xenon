package me.av306.xenon.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin( PlayerEntity.class )
public class PlayerEntityMixin
{
    /*@Inject(
            at = @At( "HEAD" ),
            method = "onKilledOther(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)Z"
    )
    private void onPlayerOnKilledOther(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir )
    {
        PlayerKillEntityEvent.EVENT.invoker().interact( world, other );
        Xenon.INSTANCE.sendInfoMessage( "Mixin invoked" );
    }*/
}
