package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.features.interfaces.IUpdatableFeature;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Vec3d;


public class ProximityRadarFeature extends IUpdatableFeature
{


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

                          if (
                                  (entityPos.getX() < Xenon.INSTANCE.CLIENT.player.getPos().getX() + 50 || entityPos.getX() > Xenon.INSTANCE.CLIENT.player.getPos().getX() - 50) &&
                                  (entityPos.getY() < Xenon.INSTANCE.CLIENT.player.getPos().getY() + 50 || entityPos.getY() > Xenon.INSTANCE.CLIENT.player.getPos().getY() - 50) &&
                                  (entityPos.getZ() < Xenon.INSTANCE.CLIENT.player.getPos().getZ() + 50 || entityPos.getZ() > Xenon.INSTANCE.CLIENT.player.getPos().getZ() - 50)
                          )
                          {
                              Xenon.INSTANCE.client.player.sendMessage( new LiteralText( "WARNING! PROJECTILE IN RANGE" ), false );
                          }
                      }
                  }
            );
    }
}
