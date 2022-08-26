package me.av306.xenon.mixin;

import com.mojang.authlib.GameProfile;
import me.av306.xenon.event.ChatOutputEvent;
import me.av306.xenon.event.ClientPlayerTickEvents;
import me.av306.xenon.event.EventFields;
import me.av306.xenon.event.GetJumpVelocityEvent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
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

    // code from an unknwown (post-1.18, pre-1.19) version
    // (chat reports in-progress build)
    /*
    @Inject(
            at = @At( "HEAD" ),
            method = "sendChatMessagePacket(Ljava/lang/String;Lnet/minecraft/text/Text;)V",
            cancellable = true
    )
    private void onSendChatMessagePacket( String message, @Nullable Text previewCallbackInfo, CallbackInfo ci )
    {
        //Xenon.INSTANCE.LOGGER.info( "msg" );
        ActionResult result = ChatOutputEvent.EVENT.invoker().interact( message );

        if ( result == ActionResult.FAIL )
            ci.cancel();
    }
    */

    // 1.19 code
    /*
    @Inject(
            at = @At( "HEAD" ),
            method = "sendChatMessagePacket(Lnet/minecraft/network/message/ChatMessageSigner;Ljava/lang/String;Lnet/minecraft/text/Text;)V",
            cancellable = true
    )
    private void onSendChatMessagePacket (net.minecraft.network.message.ChatMessageSigner signer, String message, @Nullable Text previewCallbackInfo, CallbackInfo ci )
    {
        //Xenon.INSTANCE.LOGGER.info( "msg" );
        ActionResult result = ChatOutputEvent.EVENT.invoker().interact( message );

        if ( result == ActionResult.FAIL )
            ci.cancel();
    }
    */

    // 1.19.1++ code
    @Inject(
            at = @At( "HEAD" ),
            method = "sendChatMessage(Ljava/lang/String;Lnet/minecraft/text/Text;)V",
            cancellable = true
    )
    private void onSendChatMessage(String message, Text preview, CallbackInfo ci )
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
        ActionResult result = ClientPlayerTickEvents.START_PLAYER_TICK.invoker().onStartPlayerTick();

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
        ActionResult result = ClientPlayerTickEvents.END_PLAYER_TICK.invoker().onEndPlayerTick();

        if ( result == ActionResult.FAIL )
            ci.cancel();
	}

    /*@Override
    public boolean onKilledOther( ServerWorld serverWorld, LivingEntity otherEntity )
    {
        PlayerKillEntityEvent.EVENT.invoker().interact( serverWorld, otherEntity );
        Xenon.INSTANCE.sendInfoMessage( "Method override invoked" );
        return super.onKilledOther( serverWorld, otherEntity );
    }

    @Override
    protected void onKilledBy( @Nullable LivingEntity adversary)
    {

        Xenon.INSTANCE.sendInfoMessage( "player killed,Method override invoked" );
        super.onKilledBy(adversary);
    }*/
}
