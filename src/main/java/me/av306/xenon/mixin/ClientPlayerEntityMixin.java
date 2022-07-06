package me.av306.xenon.mixin;

import com.mojang.authlib.GameProfile;
import me.av306.xenon.Xenon;
import me.av306.xenon.event.ChatOutputEvent;
import me.av306.xenon.event.EventFields;
import me.av306.xenon.event.GetJumpVelocityEvent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.network.message.ChatMessageSigner;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( ClientPlayerEntity.class )
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity
{
    public ClientPlayerEntityMixin( ClientWorld world, GameProfile profile, PlayerPublicKey key )
    {
        super( world, profile, key );
    }

    @Override
    public float getJumpVelocity()
    {
        GetJumpVelocityEvent.EVENT.invoker().interact();
        return super.getJumpVelocity() + EventFields.JUMP_VELOCITY_MODIFIER;
    }

    // Old (pre-1.19) event
    /*@Inject(
            at = @At( "HEAD" ),
            method = "sendChatMessage(Ljava/lang/String;)V",
            cancellable = true
    )
    private void onSendChatMessage( String message, CallbackInfo ci )
    {
        Xenon.INSTANCE.LOGGER.info( "msg" );
        ActionResult result = ChatOutputEvent.EVENT.invoker().interact( message );

        if ( result == ActionResult.FAIL )
            ci.cancel();
    }*/

    @Inject(
            at = @At( "HEAD" ),
            method = "sendChatMessagePacket(Lnet/minecraft/network/message/ChatMessageSigner;Ljava/lang/String;Lnet/minecraft/text/Text;)V",
            cancellable = true
    )
    private void onSendChatMessagePacket( ChatMessageSigner signer, String message, @Nullable Text previewCallbackInfo, CallbackInfo ci )
    {
        //Xenon.INSTANCE.LOGGER.info( "msg" );
        ActionResult result = ChatOutputEvent.EVENT.invoker().interact( message );

        if ( result == ActionResult.FAIL )
            ci.cancel();
    }

    @Inject(
            at = @At( "HEAD" ),
            method = "tick()V",
            cancellable = true
    )
	private void onStartPlayerTick( CallbackInfo ci )
	{
        ActionResult r = ClientPlayerTickEvent.START_PLAYER_TICK.invoker().onStartPlayerTick();

        if ( result == ActionResult.FAIL )
            ci.cancel();
	}

    @Inject(
            at = @At(
                    value = "INVOKE",
		            target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V",
		            ordinal = 0
            ),
            method = "tick()V",
            cancellable = true
    )
	private void onEndPlayerTick( CallbackInfo ci )
	{
        ActionResult r = ClientPlayerTickEvent.END_PLAYER_TICK.invoker().onEndPlayerTick();

        if ( result == ActionResult.FAIL )
            ci.cancel();
	}
}
