package me.av306.xenon.mixin;

import me.av306.xenon.Xenon;

import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/**
 * This is just here for documentation purposes.
 * It's not even registered in the mixins file.
 * It serves ABSOLUTELY no purpose besides to remind me how I'm supposed to write mixins.
 * :D
 */
@Mixin(TitleScreen.class)
public class ExampleMixin
{
	@Inject(at = @At("HEAD"), method = "init()V")
	private void init( CallbackInfo info )
	{
		Xenon.INSTANCE.LOGGER.info("This line is printed by an example mod mixin!");
	}
}