package me.av306.xenon.mixin;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.MinecraftClientEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.client.WindowEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( MinecraftClient.class )
public abstract class MinecraftClientMixin extends ReentrantThreadExecutor<Runnable> implements WindowEventHandler
{
    public MinecraftClientMixin( String s ) { super( s ); }
    
    @Inject(
            at = @At( "HEAD" ),
            method = "joinWorld(Lnet/minecraft/client/world/ClientWorld;)V"
    )
    private void onJoinWorld( ClientWorld world, CallbackInfo ci )
    {
        // This happens really early, right after the "logged in at position" message
        // So chat messages won't show
        MinecraftClientEvents.JOIN_WORLD.invoker().onJoinWorld( world );
    }

    @Inject(
            at = @At( "TAIL" ),
            method = "joinWorld(Lnet/minecraft/client/world/ClientWorld;)V"
    )
    private void onJoinWorldTail( ClientWorld world, CallbackInfo ci )
    {
        MinecraftClientEvents.JOIN_WORLD_TAIL.invoker().onJoinWorld( world );
    }

    @Inject(
            at = @At( "HEAD" ),
            method = "disconnect()V"
    )
    private void onDisconnect( CallbackInfo ci )
    {
        MinecraftClientEvents.DISCONNECT.invoker().onDisconnect();
        // This is different from ClientWorld#disconnect(), this is less reliable
    }
}
