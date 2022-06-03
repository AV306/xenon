package me.av306.xenon.mixin;

import me.av306.xenon.event.ChatInputEvent;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( ChatHud.class )
public class ChatHudMixin
{
    @Inject(
            at = @At( "HEAD" ),
            method = "addMessage(Lnet/minecraft/text/Text;)V",
            cancellable = true
    )
    private void onAddMessage( Text text, CallbackInfo ci )
    {
        ActionResult result = ChatInputEvent.EVENT.invoker().interact( text );
        if ( result == ActionResult.FAIL )
            ci.cancel();
    }
}
