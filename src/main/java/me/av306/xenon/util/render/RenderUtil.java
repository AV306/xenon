package me.av306.xenon.util.render;

import net.minecraft.util.math.*;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import me.av306.xenon.Xenon;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferBuilder.BuiltBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import net.minecraft.world.chunk.Chunk;

public class RenderUtil
{
	// Zero-size box
	private static final Box ZERO_SIZE_BOX = new Box( 0, 0, 0, 1, 1, 1 );

	// 1x1 box, centered on the origin and resting on the surface
	public static final Box DEFAULT_BOX = new Box( -0.5, 0, -0.5, 0.5, 1, 0.5 );

	public static void scissorBox( int startX, int startY, int endX, int endY )
	{
		int width = endX - startX;
		int height = endY - startY;
		int bottomY = Xenon.INSTANCE.client.currentScreen.height - endY;
		double factor = Xenon.INSTANCE.client.getWindow().getScaleFactor();

		int scissorX = (int) (startX * factor);
		int scissorY = (int) (bottomY * factor);
		int scissorWidth = (int) (width * factor);
		int scissorHeight = (int) (height * factor);
		GL11.glScissor( scissorX, scissorY, scissorWidth, scissorHeight );
	}

	public static void applyRenderOffset( MatrixStack matrixStack )
	{
		Vec3d camPos = getCameraPos();

		matrixStack.translate( -camPos.x, -camPos.y, -camPos.z );
	}

	public static void applyRegionalRenderOffset( MatrixStack matrixStack )
	{
		Vec3d camPos = getCameraPos();
		BlockPos blockPos = getCameraBlockPos();

		int regionX = (blockPos.getX() >> 9) * 512;
		int regionZ = (blockPos.getZ() >> 9) * 512;

		matrixStack.translate(
				regionX - camPos.x, -camPos.y,
				regionZ - camPos.z
		);
	}

	public static void applyRegionalRenderOffset( MatrixStack matrixStack, Chunk chunk )
	{

		Vec3d camPos = getCameraPos();

		int regionX = (chunk.getPos().getStartX() >> 9) * 512;
		int regionZ = (chunk.getPos().getStartZ() >> 9) * 512;

		matrixStack.translate(
				regionX - camPos.x, -camPos.y,
				regionZ - camPos.z
		);
	}

	public static Vec3d getCameraPos()
	{
		Camera camera = Xenon.INSTANCE.client.getBlockEntityRenderDispatcher().camera;
		if ( camera == null ) return Vec3d.ZERO;

		return camera.getPos();
	}

	public static BlockPos getCameraBlockPos()
	{
		Camera camera = Xenon.INSTANCE.client.getBlockEntityRenderDispatcher().camera;
		if ( camera == null ) return BlockPos.ORIGIN;

		return camera.getBlockPos();
	}

	public static void drawSolidBox( MatrixStack matrixStack )
	{
		drawSolidBox( ZERO_SIZE_BOX, matrixStack );
	}

	public static void drawSolidBox( Box b, MatrixStack matrixStack )
	{
		// Get the position matrix
		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		// Get the current tesselator
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		// Get the tesselator's buffer
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		// Set the shader
		RenderSystem.setShader(GameRenderer::getPositionProgram);

		// Begin!
		bufferBuilder.begin( VertexFormat.DrawMode.QUADS, VertexFormats.POSITION );


		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.minY, (float) b.minZ ).next();
		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.minY, (float) b.minZ ).next();
		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.minY, (float) b.maxZ ).next();
		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.minY, (float) b.maxZ ).next();

		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.maxY, (float) b.minZ ).next();
		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.maxY, (float) b.maxZ ).next();
		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.maxY, (float) b.maxZ ).next();
		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.maxY, (float) b.minZ ).next();

		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.minY, (float) b.minZ ).next();
		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.maxY, (float) b.minZ ).next();
		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.maxY, (float) b.minZ ).next();
		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.minY, (float) b.minZ ).next();

		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.minY, (float) b.minZ ).next();
		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.maxY, (float) b.minZ ).next();
		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.maxY, (float) b.maxZ ).next();
		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.minY, (float) b.maxZ ).next();

		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.minY, (float) b.maxZ ).next();
		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.minY, (float) b.maxZ ).next();
		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.maxY, (float) b.maxZ ).next();
		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.maxY, (float) b.maxZ ).next();

		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.minY, (float) b.minZ ).next();
		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.minY, (float) b.maxZ ).next();
		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.maxY, (float) b.maxZ ).next();
		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.maxY, (float) b.minZ ).next();
		tessellator.draw();
	}

	public static void drawSolidBox( Box b, VertexBuffer vertexBuffer )
	{
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		bufferBuilder.begin( VertexFormat.DrawMode.QUADS, VertexFormats.POSITION );
		drawSolidBox( b, bufferBuilder );
		BuiltBuffer buffer = bufferBuilder.end();

		vertexBuffer.bind();
		vertexBuffer.upload( buffer );
		VertexBuffer.unbind();
	}

	public static void drawSolidBox( Box b, BufferBuilder bufferBuilder )
	{
		bufferBuilder.vertex( b.minX, b.minY, b.minZ ).next();
		bufferBuilder.vertex( b.maxX, b.minY, b.minZ ).next();
		bufferBuilder.vertex( b.maxX, b.minY, b.maxZ ).next();
		bufferBuilder.vertex( b.minX, b.minY, b.maxZ ).next();

		bufferBuilder.vertex( b.minX, b.maxY, b.minZ ).next();
		bufferBuilder.vertex( b.minX, b.maxY, b.maxZ ).next();
		bufferBuilder.vertex( b.maxX, b.maxY, b.maxZ ).next();
		bufferBuilder.vertex( b.maxX, b.maxY, b.minZ ).next();

		bufferBuilder.vertex( b.minX, b.minY, b.minZ ).next();
		bufferBuilder.vertex( b.minX, b.maxY, b.minZ ).next();
		bufferBuilder.vertex( b.maxX, b.maxY, b.minZ ).next();
		bufferBuilder.vertex( b.maxX, b.minY, b.minZ ).next();

		bufferBuilder.vertex( b.maxX, b.minY, b.minZ ).next();
		bufferBuilder.vertex( b.maxX, b.maxY, b.minZ ).next();
		bufferBuilder.vertex( b.maxX, b.maxY, b.maxZ ).next();
		bufferBuilder.vertex( b.maxX, b.minY, b.maxZ ).next();

		bufferBuilder.vertex( b.minX, b.minY, b.maxZ ).next();
		bufferBuilder.vertex( b.maxX, b.minY, b.maxZ ).next();
		bufferBuilder.vertex( b.maxX, b.maxY, b.maxZ ).next();
		bufferBuilder.vertex( b.minX, b.maxY, b.maxZ ).next();

		bufferBuilder.vertex( b.minX, b.minY, b.minZ ).next();
		bufferBuilder.vertex( b.minX, b.minY, b.maxZ ).next();
		bufferBuilder.vertex( b.minX, b.maxY, b.maxZ ).next();
		bufferBuilder.vertex( b.minX, b.maxY, b.minZ ).next();
	}

	public static void drawOutlinedBox( MatrixStack matrixStack )
	{
		drawOutlinedBox( ZERO_SIZE_BOX, matrixStack );
	}

	public static void drawOutlinedBox( Box b, MatrixStack matrixStack )
	{
		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader( GameRenderer::getPositionProgram );

		bufferBuilder.begin( VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION );
		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.minY, (float) b.minZ ).next();
		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.minY, (float) b.minZ ).next();

		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.minY, (float) b.minZ ).next();
		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.minY, (float) b.maxZ ).next();

		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.minY, (float) b.maxZ ).next();
		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.minY, (float) b.maxZ ).next();

		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.minY, (float) b.maxZ ).next();
		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.minY, (float) b.minZ ).next();

		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.minY, (float) b.minZ ).next();
		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.maxY, (float) b.minZ ).next();

		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.minY, (float) b.minZ ).next();
		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.maxY, (float) b.minZ ).next();

		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.minY, (float) b.maxZ ).next();
		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.maxY, (float) b.maxZ ).next();

		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.minY, (float) b.maxZ ).next();
		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.maxY, (float) b.maxZ ).next();

		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.maxY, (float) b.minZ ).next();
		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.maxY, (float) b.minZ ).next();

		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.maxY, (float) b.minZ ).next();
		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.maxY, (float) b.maxZ ).next();

		bufferBuilder.vertex( matrix, (float) b.maxX, (float) b.maxY, (float) b.maxZ ).next();
		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.maxY, (float) b.maxZ ).next();

		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.maxY, (float) b.maxZ ).next();
		bufferBuilder.vertex( matrix, (float) b.minX, (float) b.maxY, (float) b.minZ ).next();
		tessellator.draw();
	}

	public static void drawOutlinedBox( Box b, VertexBuffer vertexBuffer )
	{
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		bufferBuilder.begin( VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION );
		drawOutlinedBox( b, bufferBuilder );
		BuiltBuffer buffer = bufferBuilder.end();

		vertexBuffer.bind();
		vertexBuffer.upload( buffer );
		VertexBuffer.unbind();
	}

	public static void drawOutlinedBox( Box b, BufferBuilder bufferBuilder )
	{
		bufferBuilder.vertex( b.minX, b.minY, b.minZ ).next();
		bufferBuilder.vertex( b.maxX, b.minY, b.minZ ).next();

		bufferBuilder.vertex( b.maxX, b.minY, b.minZ ).next();
		bufferBuilder.vertex( b.maxX, b.minY, b.maxZ ).next();

		bufferBuilder.vertex( b.maxX, b.minY, b.maxZ ).next();
		bufferBuilder.vertex( b.minX, b.minY, b.maxZ ).next();

		bufferBuilder.vertex( b.minX, b.minY, b.maxZ ).next();
		bufferBuilder.vertex( b.minX, b.minY, b.minZ ).next();

		bufferBuilder.vertex( b.minX, b.minY, b.minZ ).next();
		bufferBuilder.vertex( b.minX, b.maxY, b.minZ ).next();

		bufferBuilder.vertex( b.maxX, b.minY, b.minZ ).next();
		bufferBuilder.vertex( b.maxX, b.maxY, b.minZ ).next();

		bufferBuilder.vertex( b.maxX, b.minY, b.maxZ ).next();
		bufferBuilder.vertex( b.maxX, b.maxY, b.maxZ ).next();

		bufferBuilder.vertex( b.minX, b.minY, b.maxZ ).next();
		bufferBuilder.vertex( b.minX, b.maxY, b.maxZ ).next();

		bufferBuilder.vertex( b.minX, b.maxY, b.minZ ).next();
		bufferBuilder.vertex( b.maxX, b.maxY, b.minZ ).next();

		bufferBuilder.vertex( b.maxX, b.maxY, b.minZ ).next();
		bufferBuilder.vertex( b.maxX, b.maxY, b.maxZ ).next();

		bufferBuilder.vertex( b.maxX, b.maxY, b.maxZ ).next();
		bufferBuilder.vertex( b.minX, b.maxY, b.maxZ ).next();

		bufferBuilder.vertex( b.minX, b.maxY, b.maxZ ).next();
		bufferBuilder.vertex( b.minX, b.maxY, b.minZ ).next();
	}

	public static void drawLine( Vec3d start, Vec3d end, MatrixStack matrixStack )
	{
		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader( GameRenderer::getPositionProgram );

		bufferBuilder.begin( VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION );
		bufferBuilder.vertex( matrix, (float) start.getX(), (float) start.getY(), (float) start.getZ() ).next();
		bufferBuilder.vertex( matrix, (float) end.getX(), (float) end.getY(), (float) end.getZ() ).next();

		tessellator.draw();
	}
}
