package me.av306.xenon.features.render;

import me.av306.Xenon;
import me.av306.xenon.events.ClientPlayerTickEvents;
import me.av306.xenon.events.PlayerMotionEvents;

import net.minecraft.entity.MovementType;
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
            Camera camera = Xenon.INSTANCE.client.gameRenderer.getCamera();

            ((CameraAccessor) camera).forceMoveBy(
                movement.getX(),
                movement.getY(),
                movement.getZ()
            );

            // Cancel it so the player doesn't move
            return ActionResult.FAIL;
        }
        else return ActionResult.PASS;
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