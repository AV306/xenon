package me.av306.xenon.features.render;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.ZoomGroup;
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
    //private SimpleOptionAccessor<Double> mouseSensitivityAccessor = null;

    private Double originalMouseSensitivity; // should be null when NOT zooming; indicates we can use the current one

    public ZoomFeature()
    {
        // WI-zoom like implementation of zoom,
        // designed to work with Xenon's FOV-modifying features
        // e.g. Australian mode.
        super( "Zoom" );

        MouseScrollEvent.EVENT.register( this::onMouseScroll );
        ScrollInHotbarEvent.EVENT.register( this::onScrollInHotbar );
        GetFovEvent.EVENT.register( this::onGetFov );

        //@SuppressWarnings( "unchecked" )
        //this.mouseSensitivityAccessor = (SimpleOptionAccessor<Double>) (Object) Xenon.INSTANCE.client.options.getMouseSensitivity();
    }

    private ActionResult onGetFov( Camera camera, float td, boolean changing )
    {
        /*try
        {
            if ( this.keyBinding.isPressed() )
            {
                EventFields.FOV_ZOOM_LEVEL = MathHelper.clamp(
                    EventFields.FOV_ZOOM_LEVEL,
                    ZoomGroup.minZoom,
                    ZoomGroup.maxZoom
                );

                Xenon.INSTANCE.client.options.getMouseSensitivity()
                        .setValue( this.originalMouseSensitivity / EventFields.FOV_ZOOM_LEVEL );
            }
            else
            {
                EventFields.FOV_ZOOM_LEVEL = 1d;

                // reset sensitivity
                Xenon.INSTANCE.client.options.getMouseSensitivity()
                        .setValue( this.originalMouseSensitivity );

                // set it to null when zoom is disabled,
                // so that the next time zoom is enabled,
                // the original mouse sensitivity will be set.
                this.originalMouseSensitivity = null;
            }
        }
        catch ( NullPointerException npe )
        {
            // originalMouseSensitivity probably null,
            // set it
            this.originalMouseSensitivity = Xenon.INSTANCE.client.options.getMouseSensitivity().getValue();
        }*/

        if ( this.keyBinding.isPressed() )
        {
            if ( this.originalMouseSensitivity == null )
                this.originalMouseSensitivity = Xenon.INSTANCE.client.options.getMouseSensitivity().getValue();
            
            // clamp it
            EventFields.FOV_ZOOM_LEVEL = MathHelper.clamp(
                EventFields.FOV_ZOOM_LEVEL,
                ZoomGroup.minZoom,
                ZoomGroup.maxZoom
            );

            // adjust mouse ssensitivity based on zoom
            Xenon.INSTANCE.client.options.getMouseSensitivity()
                    .setValue( this.originalMouseSensitivity / EventFields.FOV_ZOOM_LEVEL );

        }
        else
        {
            // reset zoom
            EventFields.FOV_ZOOM_LEVEL = 1d;

            // reset sensitivity
            Xenon.INSTANCE.client.options.getMouseSensitivity()
                    .setValue( this.originalMouseSensitivity );

            // set it to null when zoom is disabled,
            // so that the next time zoom is enabled,
            // the original mouse sensitivity will be set.
            this.originalMouseSensitivity = null;
        }

        return ActionResult.PASS;
    }

    private ActionResult onScrollInHotbar( double amount )
    {
        // cancel the hotbar scroll if this is enabled
        // and consume it for our zoom adjustment.
        if ( this.keyBinding.isPressed() ) return ActionResult.FAIL;
        else return ActionResult.PASS;
    }

    private ActionResult onMouseScroll( long window, double horizontal, double vertical )
    {
        // If anyone knows how to do a horizontal scroll,
        // please email me.

        // if our key is pressed, set the zoom level
        if ( this.keyBinding.isPressed() )
        {
            // change our zoom level based on mouse scroll
            if ( vertical > 0 ) EventFields.FOV_ZOOM_LEVEL *= 1.1;
            else if ( vertical < 0 ) EventFields.FOV_ZOOM_LEVEL *= 0.9;
        }

        // pass
        return ActionResult.PASS;
    }

    @Override
    protected void keyEvent() {}

    @Override
    protected void onEnable() {}
}
