package me.av306.xenon.feature;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.template.IUpdatableFeature;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Vec3d;


public class ProximityRadarFeature extends IUpdatableFeature
{
    public String name = "ProximityRadar";

    // gets used when the key is pressed
    @Override
    public void onEnable()
    {
    }


    @Override
    public void onDisable()
    {
    }


    @Override
    public void onUpdate()
    {
		Xenon.INSTANCE.client.world.getEntities()
            .forEach((entity) ->
                  {
                      if ( entity instanceof ProjectileEntity )
                      {
                          ProjectileEntity projectileEntity = (ProjectileEntity) entity;
                          Vec3d entityPos = projectileEntity.getPos();
													Vec3d clientPos = Xenon.INSTANCE.client.player.getPos();

                          if (
                                  (entityPos.getX() < clientPos.getX() + 50 || entityPos.getX() > clientPos.getX() - 50) &&
                                  (entityPos.getY() < clientPos.getY() + 50 || entityPos.getY() > clientPos.getY() - 50) &&
                                  (entityPos.getZ() < clientPos.getZ() + 50 || entityPos.getZ() > clientPos.getZ() - 50)
                          )
                          {
                              Xenon.INSTANCE.client.player.sendMessage( new LiteralText( "WARNING! PROJECTILE IN RANGE" ), false );
                          }
                      }
                  }
            );
    }
}
