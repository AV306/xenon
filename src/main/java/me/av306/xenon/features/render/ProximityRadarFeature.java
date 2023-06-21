package me.av306.xenon.features.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.render.ProximityRadarGroup;
import me.av306.xenon.event.GameRenderEvents;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.render.RenderUtil;
import me.av306.xenon.util.render.RotationUtil;
import me.av306.xenon.util.text.TextFactory;
import me.shedaniel.math.Color;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;


public class ProximityRadarFeature extends IToggleableFeature
{
    private int ticks = 0;

    private double closestDistance = ProximityRadarGroup.range;
    private EntityScanResult type = EntityScanResult.NONE;

    public ProximityRadarFeature()
    {
        super( "ProximityRadar", "proxradar", "pr" );

        // Perform entity scanning in the render thread
        // because they're gonna be rendered somewhere before this anyway,
        // and we're rendering stuff too
        GameRenderEvents.RENDER_WORLD.register(
                (tickDelta, limitTime, matrices) -> this.scanEntities( matrices )
        );
    }

    @Override
    public void onEnable() {}


    @Override
    public void onDisable()
    {
    }

    private ActionResult scanEntities( MatrixStack matrices )
    {
        // FIXME: figure out how to optimise this
        //final int interval = ProximityRadarGroup.interval;
        //ticks++;

        if (
                /*ticks >= interval &&*/ this.isEnabled
                && Xenon.INSTANCE.client.world != null && Xenon.INSTANCE.client.player != null
        )
        {
            ticks = 0; // Reset counter

            // GL stuff
            GL11.glEnable( GL11.GL_BLEND );
            GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
            GL11.glEnable( GL11.GL_LINE_SMOOTH );
            GL11.glClear( GL11.GL_DEPTH_BUFFER_BIT );

            matrices.push();

            RenderUtil.applyRenderOffset( matrices );

            // Scan each entity
            Xenon.INSTANCE.client.world.getEntities()
                    .forEach( (entity) -> this.scanEntityAndRenderHighlight( matrices, entity ) );

            // Process the data and send the appropriate message
            Text text;
            switch ( type )
            {
                case HOSTILE ->
                {
                    text = TextFactory.createTranslatable(
                            "text.xenon.proximityradar.hostile",
                            Double.toString( closestDistance ).substring( 0, 3 )
                    ).formatted( Formatting.RED, Formatting.BOLD );

                    Xenon.INSTANCE.client.player.sendMessage( text,  true );
                }

                case PLAYER ->
                {
                    text = TextFactory.createTranslatable(
                            "text.xenon.proximityradar.player",
                            Double.toString( closestDistance ).substring( 0, 3 )
                    ).formatted( Formatting.RED, Formatting.BOLD );

                    Xenon.INSTANCE.client.player.sendMessage( text,  true );
                }
            }

            // Reset the closest detected distance and type
            this.type = EntityScanResult.NONE;
            this.closestDistance = ProximityRadarGroup.range;

            matrices.pop();

            GL11.glDisable( GL11.GL_BLEND );
            GL11.glDisable( GL11.GL_LINE_SMOOTH );

            // Reset shader color
            RenderSystem.setShaderColor( 1f, 1f, 1f, 1f );
        }

        return ActionResult.PASS;
    }


    /**
     * Scans an entity to check its type and distance from the player, and render the box and tracer.
     * @param matrices: Transformation matrix stack
     * @param entity: Entity to scan
     */
    private void scanEntityAndRenderHighlight( MatrixStack matrices, Entity entity )
    {
        final int range = ProximityRadarGroup.range;

        Vec3d entityPos = entity.getPos();
        Vec3d clientPos = Xenon.INSTANCE.client.player.getPos();
        double distance = entityPos.distanceTo( clientPos );

        if ( distance < range )
        {
            if ( entity instanceof HostileEntity )
            {
                if ( distance < closestDistance )
                {
                    closestDistance = distance;
                    type = EntityScanResult.HOSTILE;
                }


                if ( ProximityRadarGroup.showBox )
                    drawEntityBox( entity, matrices, ProximityRadarGroup.hostileBoxColor );

                if ( ProximityRadarGroup.showTracer )
                    drawEntityTracer( entity, matrices, ProximityRadarGroup.hostileBoxColor );
            }
            else if ( entity instanceof PlayerEntity && ProximityRadarGroup.detectPlayers && entity != Xenon.INSTANCE.client.player )
            {
                if ( distance < closestDistance )
                {
                    closestDistance = distance;
                    type = EntityScanResult.PLAYER;
                }


                if ( ProximityRadarGroup.showBox )
                    this.drawEntityBox( entity, matrices, ProximityRadarGroup.playerBoxColor );

                if ( ProximityRadarGroup.showTracer )
                    drawEntityTracer( entity, matrices, ProximityRadarGroup.playerBoxColor );
            }
            else if ( entity instanceof ItemEntity && ProximityRadarGroup.detectItems )
            {
                //type = EntityScanResult.ITEM;
                /*Text text = TextFactory.createTranslatable(
                        "text.xenon.proximityradar.item",
                        Double.toString( distance ).substring( 0, 3 )
                ).formatted( Formatting.RED, Formatting.BOLD );*/

                //Xenon.INSTANCE.client.player.sendMessage( text,  true );

                if ( ProximityRadarGroup.showBox )
                    this.drawEntityBox( entity, matrices, ProximityRadarGroup.itemBoxColor );

                if ( ProximityRadarGroup.showTracer )
                    drawEntityTracer( entity, matrices, ProximityRadarGroup.itemBoxColor );
            }
        }
    }

    private void drawEntityBox( Entity entity, MatrixStack matrixStack, Color color )
    {
        matrixStack.push();

        matrixStack.translate(
                entity.getPos().getX(),
                entity.getPos().getY(),
                entity.getPos().getZ()
        );
        matrixStack.scale(
                entity.getWidth() + 0.1f,
                entity.getHeight() + 0.1f,
                entity.getWidth() + 0.1f
        );



        RenderSystem.setShaderColor(
                color.getRed() / 255f,
                color.getGreen() / 255f,
                color.getBlue() / 255f,
                1f
        );
        RenderUtil.drawOutlinedBox( RenderUtil.DEFAULT_BOX, matrixStack );

        matrixStack.pop();
    }

    private void drawEntityTracer( Entity entity, MatrixStack matrixStack, Color color )
    {
        matrixStack.push();

        Vec3d start = RotationUtil.getClientLookVec().add( RenderUtil.getCameraPos() );
        Vec3d end = entity.getBoundingBox().getCenter();

        RenderSystem.setShaderColor(
                color.getRed() / 255f,
                color.getGreen() / 255f,
                color.getBlue() / 255f,
                1f
        );

        RenderUtil.drawLine( start, end, matrixStack );

        matrixStack.pop();
    }

    private enum EntityScanResult
    {
        HOSTILE,
        //NEUTRAL,
        //FRIENDLY,
        PLAYER,
        ITEM,
        NONE;
    }
}
