package me.av306.xenon.mixin;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IFeature;
import me.av306.xenon.util.render.RotationUtil;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
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
        // This works! Only fires when the player is damaged.
        //Xenon.INSTANCE.LOGGER.info( "EntityDamageS2CPacket: {}", packet.toString() );
        //Xenon.INSTANCE.LOGGER.info( "EntityDamageS2CPacket.createDamageSource(): {}", packet.createDamageSource( Xenon.INSTANCE.client.world ).toString() );
        Vec3d sourcePosition = packet.createDamageSource( Xenon.INSTANCE.client.world ).getPosition();
        Vec3d playerPosition = Xenon.INSTANCE.client.player.getPos();
        Vec3d damageVec = sourcePosition.subtract( playerPosition );

        // Find direction: Left, right, above, or behind?

        // Get pitch angle of damage vector from look vector
        // +ve for up, -ve for down
        // `pitch` is the pitch of the damage vec minus pitch of the look vec
        double pitch = Math.asin( damageVec.y / damageVec.length() ) - Xenon.INSTANCE.client.player.getPitch();

        // Get yaw angle
        // FIXME: Is yaw relative to north (-Z)?
        double yaw = Math.asin(
                new Vec3d( damageVec.x, 0, damageVec.z ).length() / // Shadow of damage vector in the XZ plane
                -damageVec.z
        ) - Xenon.INSTANCE.client.player.getYaw();

        if ( pitch >= 0 )
        {
            // Damage from above
            Xenon.INSTANCE.sendInfoMessage( "above" );
        }
        else
        {
            // Damage from below
            Xenon.INSTANCE.sendInfoMessage( "below" );
        }

        if ( yaw > 0 )
        {
            // Damage from right
            // TODO: Verify
            Xenon.INSTANCE.sendInfoMessage( "right" );
        }
        else if ( yaw < 0 )
        {
            // Damage from right
        }
        // Ignore damage from straight ahead
        Xenon.INSTANCE.sendInfoMessage( "left" );
    }
}
