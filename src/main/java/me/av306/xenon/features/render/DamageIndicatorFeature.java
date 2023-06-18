package me.av306.xenon.features.render;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.render.DamageIndicatorGroup;
import me.av306.xenon.feature.IToggleableFeature;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
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

    // Damage indicator flags
    private boolean upIndicator = false;
    private boolean downIndicator = false;
    private boolean leftIndicator = false;
    private boolean rightIndicator = false;

    public DamageIndicatorFeature()
    {
        super( "DamageIndicator", "indicator", "dmgindicator", "dmghud" );

        //EntityDamageEvent.EVENT.register( this::onEntityDamage );
        upIndicator = true; downIndicator = true; leftIndicator = true; rightIndicator = true;
        //HudRenderCallback.EVENT.register( this::onInGameHudRender );
        HudRenderCallback.EVENT.register( this::onInGameHudRender );
    }

    /**
     * Calculate the damage pitch and yaw when the player is attacked.
     * @param packet: The damage packet
     */
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

    private void onInGameHudRender( MatrixStack matrices, float tickDelta )
    {
        // Return if no indicators needed
        if ( !(upIndicator || downIndicator || leftIndicator || rightIndicator) || !this.isEnabled ) return;


        int width = Xenon.INSTANCE.client.getWindow().getScaledWidth();
        float halfWidth = width / 2f;
        int height = Xenon.INSTANCE.client.getWindow().getScaledHeight();
        float halfHeight = height / 2f;

        RenderSystem.enableBlend();
        GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );

        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder buffer = tessellator.getBuffer();

        RenderSystem.setShader( GameRenderer::getPositionProgram );
        RenderSystem.setShaderColor( 1f, 0, 0, 0.5f );

        buffer.begin( VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION );
        /*buffer.vertex( positionMatrix, width / 2f, 20, 0 ).next(); // Top corner
        buffer.vertex( positionMatrix, 20, 50 + 20, 0 ).next();
        buffer.vertex( positionMatrix, width - 20, 50 + 20, 0 ).next();*/

        float xOffset = (width - (DamageIndicatorGroup.indicatorSizeFactor * width)) / 2;
        float yOffset = (height - (DamageIndicatorGroup.indicatorSizeFactor * height)) / 2;

        if ( upIndicator )
        {

            buffer.vertex( positionMatrix, halfWidth, DamageIndicatorGroup.indicatorOffset, 0 ).next(); // Top corner
            buffer.vertex( positionMatrix, xOffset, DamageIndicatorGroup.indicatorOffset + DamageIndicatorGroup.indicatorHeight, 0 ).next(); // Bottom left corner
            buffer.vertex( positionMatrix, width - xOffset, DamageIndicatorGroup.indicatorOffset + DamageIndicatorGroup.indicatorHeight, 0 ).next(); // Bottom right corner
        }

        if ( downIndicator )
        {
            buffer.vertex( positionMatrix, 480, height - DamageIndicatorGroup.indicatorOffset, 0 ).next(); // Bottom corner
            buffer.vertex( positionMatrix, xOffset, height - DamageIndicatorGroup.indicatorOffset - DamageIndicatorGroup.indicatorHeight, 0 ).next(); // Top left corner
            buffer.vertex( positionMatrix, width - xOffset, height - DamageIndicatorGroup.indicatorOffset - DamageIndicatorGroup.indicatorHeight, 0 ).next(); // Bottom right corner
        }

        if ( leftIndicator )
        {
            buffer.vertex( positionMatrix, DamageIndicatorGroup.indicatorOffset + DamageIndicatorGroup.indicatorHeight, yOffset, 0 ).next(); // Upper corner
            buffer.vertex( positionMatrix, DamageIndicatorGroup.indicatorOffset + DamageIndicatorGroup.indicatorHeight, height - yOffset, 0 ).next(); // Bottom corner
            buffer.vertex( positionMatrix, DamageIndicatorGroup.indicatorOffset, halfHeight, 0 ).next(); // Leftmost corner (middle)
        }

        if ( rightIndicator )
        {
            buffer.vertex( positionMatrix, width - DamageIndicatorGroup.indicatorOffset, halfHeight, 0 ).next(); // Rightmost corner (middle)
            buffer.vertex( positionMatrix, width - DamageIndicatorGroup.indicatorOffset + DamageIndicatorGroup.indicatorHeight, yOffset, 0 ).next(); // Upper corner
            buffer.vertex( positionMatrix, width - DamageIndicatorGroup.indicatorOffset + DamageIndicatorGroup.indicatorHeight, height - yOffset, 0 ).next(); // Bottom corner
        }



        tessellator.draw();

        RenderSystem.setShaderColor( 1f, 1f, 1f, 1f );
        RenderSystem.disableBlend();
    }

    private ActionResult onInGameHudRenderOld( MatrixStack matrices, float tickDelta )
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
        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer  = tessellator.getBuffer();

        RenderSystem.setShader( GameRenderer::getPositionColorTexProgram );
        RenderSystem.setShaderColor( 1f, 1f, 1f, 1f );

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        buffer.vertex(positionMatrix, 20, 20, 0).color(1f, 1f, 1f, 1f).texture(0f, 0f).next();
        buffer.vertex(positionMatrix, 20, 60, 0).color(1f, 0f, 0f, 1f).texture(0f, 1f).next();
        buffer.vertex(positionMatrix, 60, 60, 0).color(0f, 1f, 0f, 1f).texture(1f, 1f).next();
        buffer.vertex(positionMatrix, 60, 20, 0).color(0f, 0f, 1f, 1f).texture(1f, 0f).next();

        tessellator.draw();

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
