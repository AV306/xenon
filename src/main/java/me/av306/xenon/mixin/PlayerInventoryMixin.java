package me.av306.xenon.mixin;

import me.av306.xenon.event.ScrollInHotbarEvent;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerInventory;

@Mixin( PlayerInventory.class )
public class PlayerInventoryMixin
{
    @Inject(
            at = @At( "HEAD" ),
            method = "scrollInHotbar(D)V",
            cancellable = true
    )
    private void onScrollInHotbar( double scrollAmount, CallbackInfo ci )
    {
        ActionResult result = ScrollInHotbarEvent.EVENT.invoker().interact( scrollAmount );

        if ( result == ActionResult.FAIL ) ci.cancel();
    }
}