package me.av306.xenon.features.render;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.render.FlightHudGroup;
import me.av306.xenon.feature.IToggleableFeature;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResult;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.hit.HitResult;

public class FlightHudFeature extends IToggleableFeature
{
    // Flags
    private boolean stall = false;
    private boolean terrain = false;
    private boolean pullUp = true;

    public FlightHudFeature()
    {
        super( "FlightHUD" );

        HudRenderCallback.EVENT.register( this::onInGameHudRender );
    }

    private void onInGameHudRender( MatrixStack matrixStack, float tickDelta )
    {

        // get data
        float[] data = this.getData( tickDelta );

        // Warnings
        this.stall = data[4] < FlightHudGroup.stallVelocityThreshold; // Trigger stall warning if VS too low
        this.pullUp = data[0] < FlightHudGroup.pullUpPitchThreshold; // Trigger pull up warning if nose too far down
        this.terrain = Xenon
    }

    private float[] getData( float tickDelta )
    {
        Vector3f velocity = Xenon.INSTANCE.client.player.getVelocity().toVector3f();
        float pitch, yaw = 0;
        float roll = Xenon.INSTANCE.client.player.getRoll();

        // If no vehicle, get angles from velocity
        Entity vehicle = Xenon.INSTANCE.client.player.getVehicle();
        if ( vehicle == null )
        {
            pitch = this.calculatePitch( velocity );
            yaw = this.calculateYaw( velocity );
        }
        else // Get angles from vehicle
        {
            pitch = vehicle.getPitch();
            yaw = vehicle.getYaw();
            // Roll is only applicable for mods like Immersive Aircraft
            // but for that we should probably make a seperate mod
        }

        float airspeed = velocity.length();

        float verticalSpeed = velocity.y();

        float altitude = FlightHudGroup.terrainThreshold;

        // Height above block directly under player
        HitResult hit = Xenon.INSTANCE.client.player.raycast( FlightHudGroup.terrainThreshold + 1, tickDelta, true ) )
        if ( hit.getType() == HitResult.Type.BLOCK ) altitude = (float) (Xenon.INSTANCE.client.player.getY() - hit.getPos().getY());
       
        return new float[]{ pitch, yaw, roll, airspeed, verticalSpeed, altitude };
    }

    private float calculatePitch( Vector3fc velocity )
    {
        // Calculate pitch from velocity
        // We don't want "view pitch"
        return (float) MathHelper.atan2(
            velocity.y(),
            (float) (velocity.x() * velocity.x() + velocity.z() * velocity.z())
        );
    }

    private float calculateYaw( Vector3fc velocity )
    {
        // Calculate velocity yaw
        return (float) Math.asin(
            -velocity.x() / new Vector3f( velocity.x(), 0, velocity.z() ).length()
        );
    }

    @Override
    protected void onEnable() {}

    @Override
    protected void onDisable() {}
}
