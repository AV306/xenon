package me.av306.xenon.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin( ClientPlayerEntity.class )
public interface ClientPlayerEntityAccessor
{
	@Accessor
	float getLastPitch();

	@Accessor
	float getLastYaw();
}
