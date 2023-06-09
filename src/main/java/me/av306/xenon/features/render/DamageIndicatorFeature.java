package me.av306.xenon.features.render;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.EntityDamageEvent;
import me.av306.xenon.event.RenderInGameHudEvent;
import me.av306.xenon.feature.IToggleableFeature;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class DamageIndicatorFeature extends IToggleableFeature
{
    private float progress = 0f;

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

    // FIXME: I think we ccan remove the pitch and yaw calculations with just the length of the side
    // like for pitch, instead of acrsining, just use the sign of the delta height
    // same for yae
    private ActionResult onEntityDamage( EntityDamageS2CPacket packet )
    {
        if ( !this.isEnabled ) return ActionResult.PASS;

        Vec3d sourcePosition = packet.createDamageSource( Xenon.INSTANCE.client.world ).getPosition();
        if ( sourcePosition == null ) return ActionResult.PASS;

        
        Vec3d playerPosition = Xenon.INSTANCE.client.player.getPos();
        Vec3d damageVec = sourcePosition.subtract( playerPosition );

        //Xenon.INSTANCE.LOGGER.info( "playerPosition: {}; sourcePosition: {}", playerPosition, sourcePosition );

        // Find direction: Left, right, above, or behind?

        // Get pitch angle (radians) of damage vector from look vector
        // +ve for up, -ve for down
        // We add the pitch instead of subtracting because player pitch direction is opposite of ours
        double pitch = MathHelper.wrapDegrees( Math.toDegrees( Math.asin( damageVec.y / damageVec.length() ) ) + Xenon.INSTANCE.client.player.getPitch() );

        // Get yaw angle of damage vector from look vector
        // +ve for right, -ve for left
        // Yaw is relative to Z+
        // Offset by like -90deg
        // Works!
        double yaw = MathHelper.wrapDegrees( Math.toDegrees( Math.asin(
                -damageVec.x /
                new Vec3d( damageVec.x, 0, damageVec.z ).length() // Length of shadow of damage vector in the XZ plane
        ) ) - Xenon.INSTANCE.client.player.getYaw() );

        if ( pitch >= 0 )
        {
            // Damage from above
            this.upIndicator = true;
            //Xenon.INSTANCE.sendInfoMessage( "above" );
        }
        else
        {
            // Damage from below
            this.downIndicator = true;
            //Xenon.INSTANCE.sendInfoMessage( "below" );
        }

        if ( yaw > 0 )
        {
            // Damage from right
            // TODO: Verify
            this.rightIndicator = true;
            //Xenon.INSTANCE.sendInfoMessage( "right" );
        }
        else if ( yaw < 0 )
        {
            // Damage from left
            this.rightIndicator = true;
            //Xenon.INSTANCE.sendInfoMessage( "left" );
        }

        // Set progress to a valid state
        this.progress = 0f;

        //Xenon.INSTANCE.LOGGER.info( "pitch: {}; yaw: {}", pitch, yaw );
    
        return ActionResult.PASS;
    }

    private ActionResult onInGameHudRender( MatrixStack matrices, float tickDelta )
    {
        /* Flowchart
            (start) ->
            Are any indicators on?
            yes ->
                Which phase is the progress counter in?
                0-1 ->
                    Render the indicators with full opacity
                    Increment progress counter by duration/deltaTime
                1-2 -> 
                    Calculate smoothed alpha
                    Render indicators with smoothed alpha
                   Increment progress counter like anove
                any other value ->
                    Return (damage packet handler will reset to a valid state)
            no -> 
                Return (nothing for us to do here)
        */
       
        if ( !this.isEnabled ) return ActionResult.PASS;

        //float deltaTime = Xenon.INSTANCE.client.getLastFrameDuration() / 1000f;
        //float deltaTime = Xenon.INSTANCE.client.getRenderTime();
        //Xenon.INSTANCE.LOGGER.info( "dt: {}", deltaTime );
        float alpha = 1f;

        matrices.push();
    
        // FIXME: not working :(
        //if ( upIndicator || downIndicator || leftIndicator || rightIndicator )
        //{
            // What phase is the progress counter in?
            /*if ( this.progress >= 0 && this.progress <= 1 )
            {
                // Full opacity (alpha 1)
                this.progress += DamageIndicatorGroup.indicatorDurationMillis / deltaTime;
            }
            else if ( this.progress > 1 && this.progress <= 2 )
            {
                // Smoothed alpha [1-0]
                // Test: Sine easing
                alpha = (float) -(Math.cos( Math.PI * (this.progress - 1) ) - 1) / 2f;
                if ( alpha < 0 ) alpha = 0;
                this.progress += DamageIndicatorGroup.indicatorFadeDurationMillis / deltaTime;
            }
            else return ActionResult.PASS;*/ // Garbage value, return
             

            // Render
            // Don't use scaled width/height
            matrices.push();
            int windowWidth = Xenon.INSTANCE.client.getWindow().getWidth();
            int windowHeight = Xenon.INSTANCE.client.getWindow().getHeight();

            Matrix4f matrix = matrices.peek().getPositionMatrix();
		    Tessellator tessellator = RenderSystem.renderThreadTesselator();
		    BufferBuilder bufferBuilder = tessellator.getBuffer();
            GL11.glClear( GL11.GL_DEPTH_BUFFER_BIT );

		    RenderSystem.setShader( GameRenderer::getPositionProgram );
            RenderSystem.setShaderColor( 1f, 0f, 0f, 1f );

            bufferBuilder.begin( VertexFormat.DrawMode.QUADS, VertexFormats.POSITION );

            Xenon.INSTANCE.sendInfoMessage( "render " );
            bufferBuilder.vertex( matrix, 0, 0, 0 ).next();
            bufferBuilder.vertex( matrix, 10000f, 0, 0 ).next();
            bufferBuilder.vertex( matrix, 10000f, 10000f, 0 ).next();
            bufferBuilder.vertex( matrix, 0, 10000f, 0 ).next();
            // Why not rendering :(

            // FIXME: Reduc calculations with cache?
            /*if ( this.upIndicator )
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
            }*/

            tessellator.draw();
            RenderSystem.setShaderColor( 1f, 1f, 1f, 1f );
            matrices.pop(); // Spent so long trying to find the double pop()
        //}
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
