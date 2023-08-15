package me.av306.xenon.features.render;

import org.joml.Matrix4f;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.GameRenderEvents;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.mixin.RenderSystemMixin;
import me.av306.xenon.util.render.RenderUtil;
import me.av306.xenon.util.render.RotationUtil;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.Heightmap;

public class NavigatorFeature extends IToggleableFeature
{
    private final Vector3f destination = new Vector3f( 0, 0, 0 );

    public NavigatorFeature()
    {
        super( "Navigator", "nav" );

        //WorldRenderEvents.LAST.register( this::onRenderWorld );
        GameRenderEvents.RENDER_WORLD.register( this::onRenderWorld );
    }

    private ActionResult onRenderWorld( float tickDelta, long limitTime, MatrixStack matrixStack )
    {
        RenderUtil.applyRenderOffset( matrixStack );
        
        RenderSystem.setShaderColor( 0, 1f, 0, 1f );

        //GL11.glClear( GL11.GL_DEPTH_BUFFER_BIT );
        //GL11.glLineWidth( 10 );
        //RenderSystem.lineWidth( 50f );
        RenderSystem.disableDepthTest();

		RenderUtil.drawLine(
            RotationUtil.getClientLookVec().add( RenderUtil.getCameraPos() ),
            new Vec3d( this.destination ),
            matrixStack
        );

        //this.sendInfoMessage( "text.xenon.navigator.debug", this.destination.x, this.destination.y, this.destination.z );
        //GL11.glLineWidth( 10 );
        //GL11.glClear( GL11.GL_DEPTH_BUFFER_BIT );
        /*//Vec3d start = RenderUtil.getCameraPos();
        Vec3d start = Xenon.INSTANCE.client.player.getPos();
        Vec3d end = new Vec3d( this.destination );

        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader( GameRenderer::getPositionColorProgram );

		bufferBuilder.begin( VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION );
		bufferBuilder.vertex( matrix, (float) start.getX(), (float) start.getY(), (float) start.getZ() ).next();
		bufferBuilder.vertex( matrix, (float) end.getX(), (float) end.getY(), (float) end.getZ() ).next();

        //bufferBuilder.vertex( start.getX(), start.getY(), start.getZ() ).color( 0, 255, 0, 255 ).next();
		//bufferBuilder.vertex( end.getX(), end.getY(), end.getZ() ).color( 0, 0, 255, 255 ).next();

		tessellator.draw();*/

        //RenderSystem.enableDepthTest();

        RenderSystem.setShaderColor( 1f, 1f, 1f, 1f );

        return ActionResult.PASS;
    }

    @Override
    protected void onEnable() {}

    @Override
    protected void onDisable() {}

    @Override
    protected boolean onRequestExecuteAction( String[] args )
    {
        try
        {
            this.enable();

            switch ( args[0] )
            {
                // Default destiation setter
                case "dest" ->
                {
                    // Mark destination point
                    int x, y, z;
                    if ( args.length == 3 )
                    {
                        // XZ provided
                        x = Integer.valueOf( args[1] );
                        z = Integer.valueOf( args[2] );
                        y = Xenon.INSTANCE.client.world.getTopY( Heightmap.Type.WORLD_SURFACE_WG, x, z );
                    }
                    else
                    {
                        x = Integer.valueOf( args[1] );
                        y = Integer.valueOf( args[2] );
                        z = Integer.valueOf( args[3] );
                    }
                    this.destination.set( x, y, z );
                }
            }
        }
        catch ( ArrayIndexOutOfBoundsException oobe )
        {
            this.disable();
            return false;
        }

        this.sendInfoMessage(
            "text.xenon.navigator.setdest.success",
            this.destination.x(),
            this.destination.y(),
            this.destination.z()
        );

        return true;
    }
}
