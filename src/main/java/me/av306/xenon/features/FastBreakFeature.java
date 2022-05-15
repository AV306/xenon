package me.av306.xenon.features;

import ca.weblite.objc.Client;
import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.FastBreakGroup;
import me.av306.xenon.event.PlayerDamageBlockEvent;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.mixin.ClientPlayerInteractionManagerAccessor;
import me.av306.xenon.mixin.ClientPlayerInteractionManagerMixin;
import me.av306.xenon.mixin.MinecraftClientAccessor;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class FastBreakFeature extends IToggleableFeature
{
    public FastBreakFeature()
    {
        super( "FastBreak" );

        PlayerDamageBlockEvent.EVENT.register( this::onUpdateBlockBreakingProgress );
    }

    private ActionResult onUpdateBlockBreakingProgress( BlockPos pos, Direction dir )
    {
        //Xenon.INSTANCE.LOGGER.info( "fastbreak" );
        if ( this.isEnabled )
        {
            ClientPlayerInteractionManager im = Xenon.INSTANCE.client.interactionManager;
            ClientPlayerInteractionManagerAccessor ima = (ClientPlayerInteractionManagerAccessor) im;

            assert ima != null;

            if ( !FastBreakGroup.onlyRemoveCooldown && ima.getCurrentBreakingProgress() < 1 )
            {
                PlayerActionC2SPacket.Action action = PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK;
                BlockPos blockPos = pos;
                Direction direction = dir;
                ima.sendPlayerActionC2SPacket( action, blockPos, direction );
            }

            ima.setBlockBreakingCooldown( 0 );
        }

        return ActionResult.PASS;
    }

    @Override
    protected void onEnable()
    {
    }

    @Override
    protected void onDisable()
    {

    }
}
