package me.av306.xenon.mixins;

import me.av306.xenon.Xenon;
import me.av306.xenon.keybinds.XenonKeybind;

import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin
{	
	@Inject(at = @At("HEAD"), method = "onKey(JIIII)V")
	private void onKey( long window, int key, int scanCode, int action, int modifiers, CallbackInfo info )
	{

	}
}