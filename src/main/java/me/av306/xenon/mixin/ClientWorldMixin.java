package me.av306.xenon.mixin;

import me.av306.xenon.event.ClientWorldEvents;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( ClientWorld.class )
public class ClientWorldMixin
{
	@Inject(
			at = @At( "HEAD" ),
			method = "disconnect()V"
	)
	private void onDisconnect( CallbackInfo ci )
	{
		// MinecraftClient#disconnect() is less reliable for this
		ClientWorldEvents.DISCONNECT.invoker().onDisconnect();
	}
}
