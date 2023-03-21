package me.av306.xenon.mixin;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin( Mouse.class )
public interface MouseAccessor
{
    //@Accessor
    //public double getCursorDeltaX();
    @Accessor( "cursorDeltaX" )
    public void setCursorDeltaX( double x );

    //@Accessor
    //public void getCursorDeltaY();
    @Accessor( "cursorDeltaY" )
    public void setCursorDeltaY( double y );
}
