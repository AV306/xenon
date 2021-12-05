package me.av306.xenon.mixins;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin( ChatHud.class )
public class ChatHudMixin
{
    @Inject(
            at = @At("HEAD"),
            method = "addMessage(Lnet/minecraft/text/Text;I)V",
            cancellable = true
    )
    private void onAddMessage( Text chatText, int chatLineId, CallbackInfo ci )
    {

    }

}
