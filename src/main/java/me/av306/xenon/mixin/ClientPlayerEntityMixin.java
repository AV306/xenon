package me.av306.xenon.mixin;

import com.mojang.authlib.GameProfile;
import me.av306.xenon.config.GeneralConfigGroup;
import me.av306.xenon.event.*;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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

    // Outdated as of 1.19.3, see ChatScreenMixin
    /*@Inject(
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
    }*/


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

    // Anyone remember what I was doing here? I sure don't.

    @Inject(
        at = @At( "HEAD" ),
        method = "sendMovementPackets()V",
        cancellable = true
    )
	private void beforeSendMovementPackets( CallbackInfo ci )
	{
        ActionResult result = PlayerMotionEvents.BEFORE_MOTION_PACKET.invoker()
		        .onPreMotion();

        if ( result == ActionResult.FAIL ) ci.cancel();
	}
	
	@Inject(
        at = @At( "TAIL" ),
        method = "sendMovementPackets()V"
    )
	private void afterSendMovementPackets( CallbackInfo ci )
	{
        PlayerMotionEvents.AFTER_MOTION_PACKET.invoker()
		        .onPostMotion();
	}

    @Inject(
        at = @At( "HEAD" ),
        method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V",
        cancellable = true
    )
    private void onMove( MovementType movementType, Vec3d movement, CallbackInfo ci )
    {
        ActionResult result = PlayerMotionEvents.PLAYER_MOVE.invoker()
                .onPlayerMove( movementType, movement );

        if ( result == ActionResult.FAIL ) ci.cancel();
    }

    @Redirect(
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;"
            ),
            method = "updateNausea"
    )
    private Screen getCurrentScreen( MinecraftClient client )
    {
        return GeneralConfigGroup.allowPortalGuis ? null : client.currentScreen;
    }
}
