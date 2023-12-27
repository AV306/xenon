package me.av306.xenon.features.render;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.render.ZoomGroup;
import me.av306.xenon.event.EventFields;
import me.av306.xenon.event.GetFovEvent;
import me.av306.xenon.event.MouseEvents;
import me.av306.xenon.event.ScrollInHotbarEvent;
import me.av306.xenon.feature.IFeature;
import net.minecraft.client.render.Camera;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.MathHelper;

public class ZoomFeature extends IFeature
{
    //private SimpleOptionAccessor<Double> mouseSensitivityAccessor = null;

    private Double originalMouseSensitivity; // should be null when NOT zooming; indicates we can use the current one

    public ZoomFeature()
    {
        // WI-zoom like implementation of zoom,
        // designed to work with Xenon's FOV-modifying features
        // e.g. Australian mode.
        super( "Zoom" );

        MouseEvents.ON_MOUSE_SCROLL.register( this::onMouseScroll );
        ScrollInHotbarEvent.EVENT.register( this::onScrollInHotbar );
        GetFovEvent.EVENT.register( this::onGetFov );

        //@SuppressWarnings( "unchecked" )
        //this.mouseSensitivityAccessor = (SimpleOptionAccessor<Double>) (Object) Xenon.INSTANCE.client.options.getMouseSensitivity();
    }

    private ActionResult onGetFov( Camera camera, float td, boolean changing )
    {
        try
        {
            if ( this.keyBinding.isPressed() )
            {
                if ( this.originalMouseSensitivity == null )
                    // Zoom has just been enabled, remember our original sensitivity
                    this.originalMouseSensitivity = Xenon.INSTANCE.client.options.getMouseSensitivity().getValue();

                // Clamp our zoom level
                // It should be 1 by default (no zoom when just enabled)
                // the clamp will boost it up to 2
                // eliminating the need for a local variable
                 GetFovEvent.EventData.FOV_ZOOM_LEVEL = MathHelper.clamp(
                    GetFovEvent.EventData.FOV_ZOOM_LEVEL,
                        ZoomGroup.minZoom,
                        ZoomGroup.maxZoom
                );

                // Adjust our mouse sensitivity based on zoom
                Xenon.INSTANCE.client.options.getMouseSensitivity()
                        .setValue( this.originalMouseSensitivity / GetFovEvent.EventData.FOV_ZOOM_LEVEL );

            }
            else
            {
                // Zoom not needed, reset zoom level
                //EventFields.FOV_ZOOM_LEVEL = 1d;
                GetFovEvent.EventData.FOV_ZOOM_LEVEL = 1f;

                // Reset sensitivity
                Xenon.INSTANCE.client.options.getMouseSensitivity()
                        .setValue( this.originalMouseSensitivity ); // npe

                // Set original mouse sensitivity to null when zoom is disabled,
                // so that the next time zoom is enabled, the original mouse sensitivity will be set
                // and if we have already reset sensitivity, it will throw and we don't need to set again
                this.originalMouseSensitivity = null;
            }
        }
        catch ( NullPointerException ignored ) {}

        return ActionResult.PASS;
    }

    private ActionResult onScrollInHotbar( double amount )
    {
        // Cancel the hotbar scroll if zoom is enabled
        // and consume it for our zoom adjustment.
        if ( this.keyBinding.isPressed() ) return ActionResult.FAIL;
        else return ActionResult.PASS;
    }

    private ActionResult onMouseScroll( long window, double horizontal, double vertical )
    {
        // If our key is pressed, adjust the zoom level
        if ( this.keyBinding.isPressed() )
        {
            // Scrolling up zooms in,
            // scrolling down zooms out
            if ( vertical > 0 ) GetFovEvent.EventData.FOV_ZOOM_LEVEL *= (1 + ZoomGroup.scrollInterval);
            else if ( vertical < 0 ) GetFovEvent.EventData.FOV_ZOOM_LEVEL *= (1 - ZoomGroup.scrollInterval);
        }

        return ActionResult.PASS;
    }

    @Override
    protected void keyEvent() {}

    @Override
    protected void onEnable() {}
}
