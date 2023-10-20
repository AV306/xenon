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
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.mob.Hoglin;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;


public class ProximityRadarFeature extends IToggleableFeature
{
    private static final double MAX_POSSIBLE_DISTANCE = Math.max( Math.max( ProximityRadarGroup.playerRange, ProximityRadarGroup.hostileRange ), ProximityRadarGroup.itemRange );
    private double closestDistance = MAX_POSSIBLE_DISTANCE;
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

        // Only run the scan if the feature is enabled and essential stuff isn't null
        if (
                this.isEnabled
                && Xenon.INSTANCE.client.world != null && Xenon.INSTANCE.client.player != null
        )
        {
            // GL stuff
            // Makes the lines appear on top of everything
            GL11.glEnable( GL11.GL_BLEND );
            GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
            GL11.glEnable( GL11.GL_LINE_SMOOTH );
            GL11.glClear( GL11.GL_DEPTH_BUFFER_BIT );

            matrices.push();

            // Idk what this does
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
            this.closestDistance = MAX_POSSIBLE_DISTANCE;

            matrices.pop();

            // Reset GL stuff
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
        // Grab entity data
        Vec3d entityPos = entity.getPos();
        Vec3d clientPos = Xenon.INSTANCE.client.player.getPos();
        double distance = entityPos.distanceTo( clientPos );

        if ( ProximityRadarGroup.detectPlayers && entity instanceof PlayerEntity && entity != Xenon.INSTANCE.client.player && distance < ProximityRadarGroup.playerRange )
        {
            if ( distance < closestDistance )
            {
                type = EntityScanResult.PLAYER;
                closestDistance = distance;
            }

            if ( ProximityRadarGroup.showPlayerBox )
                this.drawEntityBox( entity, matrices, ProximityRadarGroup.playerBoxColor );

            if ( ProximityRadarGroup.showPlayerTracer )
                this.drawEntityTracer( entity, matrices, ProximityRadarGroup.playerBoxColor );
        }
        else if (
                ProximityRadarGroup.detectHostiles &&
                (entity instanceof HostileEntity || entity instanceof EnderDragonEntity || entity instanceof EnderDragonPart || (entity instanceof Hoglin && ProximityRadarGroup.detectHoglins)) &&
                 distance < ProximityRadarGroup.hostileRange
        )
        {
            // Skip neutral zombified piglins
            if ( entity instanceof ZombifiedPiglinEntity zombifiedPiglin && !zombifiedPiglin.isAngryAt( Xenon.INSTANCE.client.player ) && !ProximityRadarGroup.detectZombifiedPiglins ) return;

            if ( distance < closestDistance )
            {
                type = EntityScanResult.HOSTILE;
                closestDistance = distance;
            }

            if ( ProximityRadarGroup.showHostileBox )
                this.drawEntityBox( entity, matrices, ProximityRadarGroup.hostileBoxColor );

            if ( ProximityRadarGroup.showHostileTracer )
                this.drawEntityTracer( entity, matrices, ProximityRadarGroup.hostileBoxColor );
        }
        else if ( ProximityRadarGroup.detectItems && entity instanceof ItemEntity && distance < ProximityRadarGroup.itemRange )
        {
            if ( ProximityRadarGroup.showItemBox )
                this.drawEntityBox( entity, matrices, ProximityRadarGroup.itemBoxColor );

            if ( ProximityRadarGroup.showItemTracer )
                this.drawEntityTracer( entity, matrices, ProximityRadarGroup.itemBoxColor );
        }
        else if ( ProximityRadarGroup.detectProjectiles && entity instanceof ProjectileEntity && distance < ProximityRadarGroup.projectileRange )
        {
            if ( ProximityRadarGroup.showProjectileBox )
                this.drawEntityBox( entity, matrices, ProximityRadarGroup.projectileBoxColor );

            if ( ProximityRadarGroup.showProjectileTracer )
                this.drawEntityTracer( entity, matrices, ProximityRadarGroup.projectileBoxColor );
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
