package me.av306.xenon.features.render;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.PlayerMotionEvents;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.mixin.CameraAccessor;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.MovementType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

public class FreeCamFeature extends IToggleableFeature
{
    public FreeCamFeature()
    {
        super( "FreeCam", "fc" );

        PlayerMotionEvents.PLAYER_MOVE.register( this::onPlayerMove );
    }

    private ActionResult onPlayerMove( MovementType movementType, Vec3d movement )
    {
        if ( this.isEnabled )
        {
            // Capture the movement vector and apply it to the camera
            Xenon.INSTANCE.client.player.noClip = true;

            // Cancel it so the player doesn't move
            //return ActionResult.FAIL;
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