package me.av306.xenon.mixin;

import com.mojang.authlib.GameProfile;
import me.av306.xenon.event.ChatOutputEvent;
import me.av306.xenon.event.EventFields;
import me.av306.xenon.event.GetJumpVelocityEvent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( ClientPlayerEntity.class )
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity
{
    public ClientPlayerEntityMixin( ClientWorld world, GameProfile profile )
    {
        super( world, profile );
    }

    @Override
    public float getJumpVelocity()
    {
        GetJumpVelocityEvent.EVENT.invoker().interact();
        return super.getJumpVelocity() + EventFields.JUMP_VELOCITY_MODIFIER;
    }

    @Inject(
            at = @At( "HEAD" ),
            method = "sendChatMessage(Ljava/lang/String;)V",
            cancellable = true
    )
    private void onSendChatMessage( String message, CallbackInfo ci )
    {
        ActionResult result = ChatOutputEvent.EVENT.invoker().interact( message );

        if ( result == ActionResult.FAIL )
            ci.cancel();
    }
}
