package me.av306.xenon.mixins;

import me.av306.xenon.Xenon;
import me.av306.xenon.features.CommandProcessorFeature;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.LiteralText;
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
        if ( CommandProcessorFeature.isEnabled && chatText.getString().startsWith("!") )
        {
            String command = chatText.getString().replace("!", " ").stripLeading();
            if ( Xenon.INSTANCE.commandRegistry.registry.containsKey(command) )
            {
                Xenon.INSTANCE.commandRegistry.registry.get(command).execute(); // TODO
                Xenon.INSTANCE.CLIENT.player.sendMessage(
                        new LiteralText( String.format( "Command: %s", command ) ),
                        false
                );
            }
        }
    }

}