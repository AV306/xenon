package me.av306.xenon.mixin;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.MinecraftClientEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
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
            method = "joinWorld(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/client/gui/screen/DownloadingTerrainScreen$WorldEntryReason;)V"
    )
    private void onJoinWorld( ClientWorld world, DownloadingTerrainScreen.WorldEntryReason worldEntryReason, CallbackInfo ci )
    {
        // This happens really early, right after the "logged in at position" message
        // So chat messages won't show
        //Xenon.INSTANCE.LOGGER.info( "client join world" );
        MinecraftClientEvents.JOIN_WORLD.invoker().onJoinWorld( world );
    }

    @Inject(
            at = @At( "TAIL" ),
            method = "joinWorld(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/client/gui/screen/DownloadingTerrainScreen$WorldEntryReason;)V"
    )
    private void onJoinWorldTail( ClientWorld world, DownloadingTerrainScreen.WorldEntryReason worldEntryReason, CallbackInfo ci )
    {
        MinecraftClientEvents.JOIN_WORLD_TAIL.invoker().onJoinWorld( world );
    }

    @Inject(
            at = @At( "HEAD" ),
            method = "disconnect()V"
    )
    private void onDisconnect( CallbackInfo ci )
    {
        //Xenon.INSTANCE.LOGGER.info( "client disconnect" );
        MinecraftClientEvents.DISCONNECT.invoker().onDisconnect();
        // This happens much later than ClientWorld#disconnect()
        // The former happens immediately after hitting "disconnect", but it
        // fires after everything else shuts down and also when the client closes
        // And after any disconnection, whether from a failed connection or intended disconnection
        // Also this fires when connecting too somehow
    }
}
