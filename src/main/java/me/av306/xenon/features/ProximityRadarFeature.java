package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.ProximityRadarGroup;
import me.av306.xenon.feature.IToggleableFeature;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.ClientPlayerTickable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Vec3d;


public class ProximityRadarFeature extends IToggleableFeature
{
    private int ticks = 0;

    public ProximityRadarFeature()
    {
        super( "ProximityRadar" );
        // tick callback in ClientPlayerTickable
        ClientTickEvents.END_WORLD_TICK.register( (object) -> this.tick() );
    }

    @Override
    public void onEnable()
    {
    }


    @Override
    public void onDisable()
    {
    }

    private void tick()
    {
        final int interval = ProximityRadarGroup.interval;

        ticks++;

        if ( ticks >= interval && this.isEnabled && Xenon.INSTANCE.client.world != null ) {
            ticks = 0;
            Xenon.INSTANCE.client.world.getEntities()
                    .forEach( this::scanEntity );
        }
    }

    private void scanEntity( Entity entity )
    {
        final int range = ProximityRadarGroup.range;

        if ( entity instanceof ProjectileEntity || entity instanceof HostileEntity )
        {
            Xenon.INSTANCE.LOGGER.info( "Scanned entity at " + entity.getBlockPos() );

            Vec3d entityPos = entity.getPos();
            Vec3d clientPos = Xenon.INSTANCE.client.player.getPos();

            if (
                    (entityPos.getX() < clientPos.getX() + range && entityPos.getX() > clientPos.getX() - range) &&
                    (entityPos.getY() < clientPos.getY() + range && entityPos.getY() > clientPos.getY() - range) &&
                    (entityPos.getZ() < clientPos.getZ() + range && entityPos.getZ() > clientPos.getZ() - range)
            )
            {
                // TODO: flash red vignette
                Xenon.INSTANCE.client.player.sendMessage( new LiteralText("WARNING! PROJECTILE IN RANGE"),  true );
            }
        }
    }
}
