package me.av306.xenon.features.render;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.EventFields;
import me.av306.xenon.event.MouseScrollEvent;
import me.av306.xenon.event.ScrollInHotbarEvent;
import me.av306.xenon.feature.IFeature;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.MathHelper;

public class ZoomFeature extends IFeature
{
    public ZoomFeature()
    {
        // WI-zoom like implementation of zoom,
        // designed to work with Xenon's FOV-modifying features
        // e.g. Australian mode.
        super( "Zoom" );

        MouseScrollEvent.EVENT.register( this::onMouseScroll );
        ScrollInHotbarEvent.EVENT.register( this::onScrollInHotbar );
    }

    private ActionResult onScrollInHotbar( double amount )
    {
        // cancel the hotbar scroll if this is enabled
        // and consume it for the zoom adjustment.
        if ( this.keyBinding.isPressed() ) return ActionResult.FAIL;
        else return ActionResult.PASS;
    }

    private ActionResult onMouseScroll( long window, double horizontal, double vertical )
    {
        // if our key is pressed, set the zoom level
        if ( this.keyBinding.isPressed() )
        {
            // change out zoom level based on mouse scroll
            if ( vertical > 0 ) EventFields.FOV_ZOOM_LEVEL *= 1.1;
            else if ( vertical < 0 ) EventFields.FOV_ZOOM_LEVEL *= 0.9;

            // clamp it down
            EventFields.FOV_ZOOM_LEVEL = MathHelper.clamp( EventFields.FOV_ZOOM_LEVEL, 2d, 50d );
        }
        else EventFields.FOV_ZOOM_LEVEL = 1d; // otherwise set it to 1

        // pass
        return ActionResult.PASS;
    }

    @Override
    protected void keyEvent()
    {
        // do nothing
        // technically the key stuff could be here,
        // but it's better to sync it to the getFov call.
    }

    @Override
    protected void onEnable()
    {

    }
}
