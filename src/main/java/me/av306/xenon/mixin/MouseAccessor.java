package me.av306.xenon.mixin;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin( Mouse.class )
public interface MouseAccessor
{
    //@Accessor
    //double getCursorDeltaX();
    @Accessor( "cursorDeltaX" )
    void setCursorDeltaX( double x );

   // @Accessor
    //void getCursorDeltaY();
    @Accessor( "cursorDeltaY" )
    void setCursorDeltaY( double y );

    @Accessor( "x" )
    void setX( double x );

    @Accessor( "y" )
    void setY( double y );

    @Accessor( "leftButtonClicked" )
    void setLeftButtonClicked( boolean clicked );

    @Accessor( "rightButtonClicked" )
    void setRightButtonClicked( boolean clicked );
}
