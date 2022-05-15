package me.av306.xenon.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

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

    @Invoker( "sendPlayerAction" )
    void sendPlayerActionC2SPacket( PlayerActionC2SPacket.Action action, BlockPos pos, Direction direction );
}
