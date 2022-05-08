package me.av306.xenon.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin( ClientPlayerInteractionManager.class )
public interface ClientPlayerInteractionManagerAccessor
{
    @Accessor
    int getBlockBreakingCooldown();

    @Accessor( "blockBreakingCooldown" )
    void setBlockBreakingCooldown( int cooldown );

    @Accessor
    float getCurrentBreakingProgress();

    @Accessor( "currentBreakingProgress" )
    void setCurrentBreakingProgress( float progress );
}
