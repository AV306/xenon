package me.av306.xenon.features.render;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.DamageIndicatorGroup;
import me.av306.xenon.event.EntityDamageEvent;
import me.av306.xenon.event.RenderInGameHudEvent;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.render.RotationUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.Camera;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

public class DamageIndicatorFeature extends IToggleableFeature
{
    private final Identifier upIndicatorId = new Identifier( "xenon", "textures/damageindicator/upIndicator.png" );
    private final Identifier leftIndicatorId = new Identifier( "xenon", "textures/damageindicator/leftIndicator.png" );

    private float indicatorProgress = 0f;

    // Damage indicator flagss
    private boolean upIndicator = false;
    private boolean downIndicator = false;
    private boolean leftIndicator = false;
    private boolean rightIndicator = false;

    public DamageIndicatorFeature()
    {
        super( "DamageIndicator", "indicator", "dmgindicator", "dmghud" );

        EntityDamageEvent.EVENT.register( this::onEntityDamage );
        RenderInGameHudEvent.AFTER_VIGNETTE.register( this::onInGameHudRender );
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

        if ( pitch >= 0 )
        {
            // Damage from above
            this.upIndicator = true;
            Xenon.INSTANCE.sendInfoMessage( "above" );
        }
        else
        {
            // Damage from below
            this.downIndicator = true;
            Xenon.INSTANCE.sendInfoMessage( "below" );
        }

        if ( yaw > 0 )
        {
            // Damage from right
            // TODO: Verify
            this.rightIndicator = true;
            Xenon.INSTANCE.sendInfoMessage( "right" );
        }
        else if ( yaw < 0 )
        {
            // Damage from left
            this.rightIndicator = true;
            Xenon.INSTANCE.sendInfoMessage( "left" );
        }

        //Xenon.INSTANCE.LOGGER.info( "pitch: {}; yaw: {}", pitch, yaw );
    
        return ActionResult.PASS;
    }

    private ActionResult onInGameHudRender( MatrixStack matrices, float tickDelta )
    {
        float deltaTime = Xenon.INSTANCE.client.getLastFrameDuration(); // FIXME: is this millis?

        if ( upIndicator || downIndicator || leftIndicator || rightIndicator )
        {
            // Skip the update and reset the flags if the duration is past
            if ( this.indicatorProgress < DamageIndicatorGroup.indicatorDurationMillis )
            {
                // TODO: Implement fade
                upIndicator = false;
                downIndicator = false;
                leftIndicator = false;
                rightIndicator = false;
                this.indicatorDurationMillis = 0f;
                return ActionResult.PASS;
            }
            
            // At least one indicator needs to be displayed and the duration isn't past

            // Increment progress
            this.indicatorProgress += deltaTime;

            // Clamp
            //if ( this.indicatorDurationMillis > 1f ) indicatorDurationMillis = 1f;

            // Render
            // Don't use scaled width/height
            matrices.push();
            int windowWidth = Xenon.INSTANCE.client.getWindow().getWidth();
            int windowHeight = Xenon.INSTANCE.client.getWindow().getHeight();

            Matrix4f matrix = matrices.peek().getPositionMatrix();
		    Tessellator tessellator = RenderSystem.renderThreadTesselator();
		    BufferBuilder bufferBuilder = tessellator.getBuffer();
		    RenderSystem.setShader( GameRenderer::getPositionProgram );
            RenderSystem.setShaderColor( 1f, 0f, 0f, 0f );
            bufferBuilder.begin( VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION );

                // FIXME: Reduc calculations with cache?
            if ( this.upIndicator )
            {
                // Left corner of the arrow
		        bufferBuilder.vertex(
                    matrix,
                    16f, // Left padding
                    64f + 5f, // height of arrow + offset
                    0f // Z0 plane
                ).next();

                // Top corner
                bufferBuilder.vertex(
                    matrix,
                    windowWidth/2f, // Middle of the screen
                    5f, // Offset
                    0f // Z0 plane
                ).next();

                // Right corner
                bufferBuilder.vertex(
                    matrix,
                    windowWidth - 16f, // Left padding
                    64f + 5f, // height of arrow + offset
                    0f // Z0 plane
                ).next();		        
            }

            if ( this.downIndicator )
            {
                // Left corner of the arrow
		        bufferBuilder.vertex(
                    matrix,
                    16f, // Left padding
                    windowWidth - (64f + 5f), // height of arrow + offset
                    0f // Z0 plane
                ).next();

                // Top corner
                bufferBuilder.vertex(
                    matrix,
                    windowWidth/2f, // Middle of the screen
                    windowHeight - 5f, // Offset
                    0f // Z0 plane
                ).next();

                // Right corner
                bufferBuilder.vertex(
                    matrix,
                    windowWidth - 16f, // Left padding
                    windowHeight - (64f + 5f), // height of arrow + offset
                    0f // Z0 plane
                ).next();		        
            }

            if ( this.leftIndicator )
            {
                // Top corner of the arrow
		        bufferBuilder.vertex(
                    matrix,
                    64f + 5f, // Width of arrow + padding
                    16f, // Top padding
                    0f // Z0 plane
                ).next();

                // Left corner
                bufferBuilder.vertex(
                    matrix,
                    5f, // Offset
                    windowHeight/2f, // Middle of screen
                    0f // Z0 plane
                ).next();

                // Right corner
                bufferBuilder.vertex(
                    matrix,
                    64f + 5f, // Width of arrow + padding
                    windowHeight - 5f, // Bottom padding
                    0f // Z0 plane
                ).next();
            }

            if ( this.rightIndicator )
            {
                // Top corner of the arrow
		        bufferBuilder.vertex(
                    matrix,
                    windowWidth - (64f + 5f), // Width of arrow + padding
                    16f, // Top padding
                    0f // Z0 plane
                ).next();

                // Left corner
                bufferBuilder.vertex(
                    matrix,
                    windowWidth - 5f, // Offset
                    windowHeight/2f, // Middle of screen
                    0f // Z0 plane
                ).next();

                // Right corner
                bufferBuilder.vertex(
                    matrix,
                    windowWidth - (64f + 5f), // Width of arrow + padding
                    windowHeight - 5f, // Bottom padding
                    0f // Z0 plane
                ).next();		        
            }

            tessellator.draw();
            matrices.pop();
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
