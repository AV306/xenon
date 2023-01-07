package me.av306.xenon.mixin;

import me.av306.xenon.event.KeyEvent;
import net.minecraft.client.Keyboard;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin
{	
	@Inject( at = @At("HEAD"), method = "onKey(JIIII)V", cancellable = true )
	private void onKey( long window, int key, int scanCode, int action, int modifiers, CallbackInfo ci )
	{
        ActionResult result = KeyEvent.EVENT.invoker().interact( window, key, scanCode, action, modifiers );

        if ( result == ActionResult.FAIL )
            ci.cancel();
	}
}
