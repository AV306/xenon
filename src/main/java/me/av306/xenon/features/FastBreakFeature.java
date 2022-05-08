package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IToggleableFeature;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class FastBreakFeature extends IToggleableFeature
{
    protected FastBreakFeature()
    {
        super( "FastBreak" );
    }

    private ActionResult onUpdateBlockBreakingProgress( BlockPos pos, Direction direction )
    {
        return ActionResult.PASS;
    }

    @Override
    protected void onEnable()
    {
        // TODO: Add CPIM onDamageBlock event, hook to that, and set the cooldown to 0 and send the following packer
        /*
        if(im.getCurrentBreakingProgress() >= 1)
			return;

		Action action = PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK;
		BlockPos blockPos = event.getBlockPos();
		Direction direction = event.getDirection();
		im.sendPlayerActionC2SPacket(action, blockPos, direction);
         */
    }

    @Override
    protected void onDisable()
    {

    }
}
