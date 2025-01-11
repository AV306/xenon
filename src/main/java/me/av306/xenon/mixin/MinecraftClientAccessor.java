package me.av306.xenon.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin( MinecraftClient.class )
public interface MinecraftClientAccessor
{
    // Not needed for 1.21-1.21.1
    //@Accessor
    //RenderTickCounter getRenderTickCounter();
}
