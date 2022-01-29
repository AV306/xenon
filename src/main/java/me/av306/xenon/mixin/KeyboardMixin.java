package me.av306.xenon.mixin;

import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Keyboard.class)
public class KeyboardMixin
{	
	/*@Inject(at = @At("HEAD"), method = "onKey(JIIII)V")
	private void onKey( long window, int key, int scanCode, int action, int modifiers, CallbackInfo info )
	{

	}*/
}
