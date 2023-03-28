package me.av306.xenon.mixin;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IFeature;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( ClientPlayNetworkHandler.class )
public class ClientPlayNetworkHandlerMixin
{
    @Inject(
            at = @At( "HEAD" ),
            method = "onChatMessage(Lnet/minecraft/network/packet/s2c/play/ChatMessageS2CPacket;)V",
            cancellable = true
    )
    private void onOnChatMessage( ChatMessageS2CPacket packet, CallbackInfo ci )
    {
        // Hook into this method to ensure that the packet is always received and processed
        // Otherwise people could just disable chat messages

        // Grab the packet content
        String content = packet.body().content();

        // Packet format: {{xenon forcedisable [name]}}
        if ( content.startsWith( "{{xenon forcedisable " ) && content.endsWith( " }}" ) )
        {
            // Get the feature name out
            content = content.replaceAll( "{{xenon forcedisable ", "" ).replaceAll( " }}", "" );
            IFeature feature = Xenon.INSTANCE.get( content );

            if ( feature == null )
            {
                Xenon.INSTANCE.LOGGER.warn( "Server requested force-disable of missing feature {}", content );
                return;
            }

            feature.setForceDisabled( true );

            // Don't process the opt-out packet
            ci.cancel();
        }
        
        // Not an opt-out packet; continue as per normal
    }
}
