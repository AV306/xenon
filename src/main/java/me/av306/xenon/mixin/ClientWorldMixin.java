package me.av306.xenon.mixin;

import me.av306.xenon.Xenon;
import me.shedaniel.math.Color;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.TextColor;
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
		Xenon.INSTANCE.LOGGER.info( "Exiting world, disabling all features" );
		Xenon.INSTANCE.disableAllFeatures();
	}
}
