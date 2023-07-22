package me.av306.xenon.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin( Camera.class )
public interface CameraAccessor
{
    @Invoker( "moveBy" )
    void forceMoveBy( double x, double y, double z );

    @Invoker( "setPos" )
    void forceSetPos( Vec3d pos );
}
