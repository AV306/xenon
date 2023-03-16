package me.av306.xenon.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin( GameRenderer.class )
public interface GameRendererAccessor
{
	@Accessor
	ResourceManager getResourceManager();
}
