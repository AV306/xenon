package me.av306.xenon.mixin;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin( Mouse.class )
public interface MouseAccessor
{

    @Accessor( "cursorDeltaX" )
    void setCursorDeltaX( double x );

    @Accessor( "cursorDeltaY" )
    void setCursorDeltaY( double y );

    @Accessor( "x" )
    void setX( double x );

    @Accessor( "y" )
    void setY( double y );

    // Somehow these aren't used???
    /*@Accessor( "leftButtonClicked" )
    void setLeftButtonClicked( boolean clicked );

    @Accessor( "rightButtonClicked" )
    void setRightButtonClicked( boolean clicked );*/
}
