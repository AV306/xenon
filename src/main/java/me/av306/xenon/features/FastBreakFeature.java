package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.mixin.ClientPlayerInteractionManagerAccessor;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

// FIXME: too complex, won;t work
public class FastBreakFeature extends IToggleableFeature
{
    protected FastBreakFeature()
    {
        super( "FastBreak" );
    }

    private ActionResult onUpdateBlockBreakingProgress( BlockPos pos, Direction dir )
    {
        // TODO: Add CPIM onDamageBlock event, hook to that, and set the cooldown to 0 and send the following packer
        ClientPlayerInteractionManager im = Xenon.INSTANCE.client.interactionManager;

        if( ((ClientPlayerInteractionManagerAccessor) im).getCurrentBreakingProgress() >= 1 )
            return ActionResult.PASS;

        PlayerActionC2SPacket.Action action = PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK;
        BlockPos blockPos = pos;
        Direction direction = dir;
        //im.sendPlayerActionC2SPacket(action, blockPos, direction);
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
