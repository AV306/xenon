package me.av306.xenon.features.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.ProximityRadarGroup;
import me.av306.xenon.event.GameRenderEvents;
import me.av306.xenon.event.RenderInGameHudEvent;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.util.render.RenderUtil;
import me.av306.xenon.util.text.TextFactory;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;


public class ProximityRadarFeature extends IToggleableFeature
{
    private int ticks = 0;

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

            GL11.glEnable( GL11.GL_BLEND );
            GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
            GL11.glEnable( GL11.GL_LINE_SMOOTH );
            //GL11.glDisable( GL11.GL_DEPTH_TEST );
            GL11.glClear( GL11.GL_DEPTH_BUFFER_BIT );

            matrices.push();

            RenderUtil.applyRenderOffset( matrices );

            // Scan each entity
            Xenon.INSTANCE.client.world.getEntities()
                    .forEach( (entity) -> this.scanEntity( matrices, entity ) );

            matrices.pop();

            GL11.glDisable( GL11.GL_BLEND );
            //GL11.glEnable( GL11.GL_DEPTH_TEST );
            GL11.glDisable( GL11.GL_LINE_SMOOTH );
        }

        return ActionResult.PASS;
    }

    private void scanEntity( MatrixStack matrices, Entity entity )
    {
        final int range = ProximityRadarGroup.range;

        if ( entity instanceof ProjectileEntity || entity instanceof HostileEntity )
        {
            Vec3d entityPos = entity.getPos();
            Vec3d clientPos = Xenon.INSTANCE.client.player.getPos();
            double distance = entityPos.distanceTo( clientPos );

            if ( distance < range )
            {
                Text text = TextFactory.createTranslatable(
                        entity instanceof HostileEntity ?
                                "text.xenon.proximityradar.hostile" :
                                "text.xenon.proximityradar.projectile",
                        Double.toString(distance).substring( 0, 3 )
                ).formatted( Formatting.RED, Formatting.BOLD );

                // TODO: flash red vignette
                Xenon.INSTANCE.client.player.sendMessage( text,  true );

                // Draw a box around the entity
                matrices.push();
                matrices.translate(
                        entityPos.getX(),
                        entityPos.getY(),
                        entityPos.getZ()
                );
                matrices.scale(
                        entity.getWidth() + 0.1f,
                        entity.getHeight() + 0.1f,
                        entity.getWidth() + 0.1f
                );

                RenderSystem.setShaderColor( 1.0f, 0.0f, 0.0f, 1.0f );

                RenderUtil.drawOutlinedBox( RenderUtil.DEFAULT_BOX, matrices );
                matrices.pop();
            }
        }
    }
}
