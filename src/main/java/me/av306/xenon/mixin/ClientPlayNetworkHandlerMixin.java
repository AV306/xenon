package me.av306.xenon.mixin;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.EntityDamageEvent;
import me.av306.xenon.feature.IFeature;
import me.av306.xenon.util.render.RotationUtil;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;
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
        // FIXME: Need to test
        // Hook into this method to ensure that the packet is always received and processed
        // Otherwise people could just disable chat messages.
        // This is about as early as we can hook

        // Grab the packet content
        String content = packet.body().content();

        // Packet format: {{xenon restrict [name]}}
        // e.g. {{xenon restrict proximityradar}}
        if ( content.startsWith( "{{xenon restrict " ) && content.endsWith( " }}" ) )
        {
            // Get the feature name out
            content = content.replaceAll( "{{xenon restrict ", "" ).replaceAll( " }}", "" );
            IFeature feature = Xenon.INSTANCE.featureRegistry.get( content );

            if ( feature == null )
            {
                Xenon.INSTANCE.LOGGER.warn( "Server requested restriction of missing feature {}", content );
                return;
            }

            feature.setForceDisabled( true );

            //Xenon.INSTANCE.sendInfoMessage( "Server restricted feature {}", content );

            // Don't continue processing the opt-out packet
            ci.cancel();
        }
        
        // Not an opt-out packet; continue as per normal
    }

    @Inject(
        at = @At( "HEAD" ),
        method = "onEntityDamage(Lnet/minecraft/network/packet/s2c/play/EntityDamageS2CPacket;)V",
        cancellable = true
    )
    private void onOnEntityDamage( EntityDamageS2CPacket packet, CallbackInfo ci )
    {
        if ( EntityDamageEvent.EVENT.invoker().interact( packet ) == ActionResult.FAIL )
            ci.cancel();
    }
}
