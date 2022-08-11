package me.av306.xenon.features.render;

import me.av306.xenon.event.EventFields;
import me.av306.xenon.event.GetFovEvent;
import me.av306.xenon.event.MouseScrollEvent;
import me.av306.xenon.event.ScrollInHotbarEvent;
import me.av306.xenon.feature.IFeature;
import net.minecraft.client.render.Camera;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.MathHelper;

public class ZoomFeature extends IFeature
{
    private double zoomLevel = 1d;

    public ZoomFeature()
    {
        // WI-zoom like implementation of zoom,
        // designed to work with Xenon's FOV-modifying features
        // e.g. Australian mode.
        super( "Zoom" );

        MouseScrollEvent.EVENT.register( this::onMouseScroll );
        ScrollInHotbarEvent.EVENT.register( this::onScrollInHotbar );
        GetFovEvent.EVENT.register( this::onGetFov );
    }

    private ActionResult onGetFov(Camera camera, float td, boolean changing )
    {
        EventFields.FOV_ZOOM_LEVEL = this.zoomLevel;
        return ActionResult.PASS;
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
            if ( vertical > 0 ) this.zoomLevel *= 1.1;
            else if ( vertical < 0 ) this.zoomLevel *= 0.9;

            // clamp it down
            this.zoomLevel = MathHelper.clamp( this.zoomLevel, 1d, 50d );
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
