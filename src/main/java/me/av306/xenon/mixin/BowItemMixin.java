package me.av306.xenon.mixin;

import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.av306.xenon.Xenon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@Mixin( BowItem.class )
public class BowItemMixin
{
    @Inject(
        at = @At( "HEAD" ),
        method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;",
        cancellable = true
    )
    private void onUse( World world, PlayerEntity plater, Hand hand, CallbackInfoReturnable<TypedActionResult> cir )
    {
        Xenon.INSTANCE.sendInfoMessage( "Bow use" );
    }

    @Inject(
        at = @At( "HEAD" ),
        method = "onStoppedUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V",
        cancellable = true
    )
    private void onStoppedUsing( ItemStack stack, World world, LivingEntity entity, int remainingUseTicks, CallbackInfo ci )
    {
        Xenon.INSTANCE.sendInfoMessage( "Bow stop use" );
    }
}
