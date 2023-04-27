package me.av306.xenon.features.render;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.EntityDamageEvent;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.render.RotationUtil;
import net.minecraft.client.render.Camera;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

public class DamageIndicatorFeature extends IToggleableFeature
{
    private float indicatorTicks = 0f;

    // Damage indicator flagss
    private boolean upIndicator = false;
    private boolean downIndicator = false;
    private boolean leftIndicator = false;
    private boolean rightIndicator = false;

    public DamageIndicatorFeature()
    {
        super( "DamageIndicator", "indicator", "dmgindicator", "dmghud" );

        EntityDamageEvent.EVENT.register( this::onEntityDamage );
    }

    private ActionResult onEntityDamage( EntityDamageS2CPacket packet )
    {   
        Vec3d sourcePosition = packet.createDamageSource( Xenon.INSTANCE.client.world ).getPosition();
        if ( sourcePosition == null ) return;

        
        Vec3d playerPosition = Xenon.INSTANCE.client.player.getPos();
        Vec3d damageVec = sourcePosition.subtract( playerPosition );

        Xenon.INSTANCE.LOGGER.info( "playerPosition: {}; sourcePosition: {}", playerPosition, sourcePosition );

        // Find direction: Left, right, above, or behind?

        // Get pitch angle (radians) of damage vector from look vector
        // +ve for up, -ve for down
        // This works!
        double pitch = Math.asin( damageVec.y / damageVec.length() ) - Xenon.INSTANCE.client.player.getPitch();

        // Get yaw angle of damage vector from look vector
        // +ve for right, -ve for left
        // FIXME: Is yaw relative to north (-Z)?
        // Offset by like -90deg
        double yaw = Math.asin(
                -damageVec.x /
                new Vec3d( damageVec.x, 0, damageVec.z ).length() // Length of shadow of damage vector in the XZ plane
        ) - Xenon.INSTANCE.client.player.getYaw();

        /*if ( pitch >= 0 )
        {
            // Damage from above
            Xenon.INSTANCE.sendInfoMessage( "above" );
        }
        else
        {
            // Damage from below
            Xenon.INSTANCE.sendInfoMessage( "below" );
        }

        if ( yaw > 0 )
        {
            // Damage from right
            // TODO: Verify
            Xenon.INSTANCE.sendInfoMessage( "right" );
        }
        else if ( yaw < 0 )
        {
            // Damage from right
            Xenon.INSTANCE.sendInfoMessage( "left" );
        }*/
        // Ignore damage from straight ahead

        Xenon.INSTANCE.LOGGER.info( "pitch: {}; yaw: {}", pitch, yaw );
    
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
