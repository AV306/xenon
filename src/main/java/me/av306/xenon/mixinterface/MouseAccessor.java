package me.av306.xenon.mixinterface;

import me.av306.xenon.event.MouseScrollEvent;
import net.minecraft.client.Mouse;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface MouseAcessor
{
    //@Accessor( "x" )
   // public void setX( double newX );

   public void changeX( double x );
   public void changeY( double y );
}
