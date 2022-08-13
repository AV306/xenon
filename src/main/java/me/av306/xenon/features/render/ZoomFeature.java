package me.av306.xenon.features.render;

import me.av306.xenon.Xenon;
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
    private double zoomLevel = 2d; // range: 2 to 50

    //private SimpleOptionAccessor<Double> mouseSensitivityAccessor = null;

    private Double originalMouseSensitivity = null;

    public ZoomFeature()
    {
        // WI-zoom like implementation of zoom,
        // designed to work with Xenon's FOV-modifying features
        // e.g. Australian mode.
        super( "Zoom" );

        MouseScrollEvent.EVENT.register( this::onMouseScroll );
        ScrollInHotbarEvent.EVENT.register( this::onScrollInHotbar );
        GetFovEvent.EVENT.register( this::onGetFov );

        this.originalMouseSensitivity = Xenon.INSTANCE.client.options.getMouseSensitivity().getValue();

        //@SuppressWarnings( "unchecked" )
        //this.mouseSensitivityAccessor = (SimpleOptionAccessor<Double>) (Object) Xenon.INSTANCE.client.options.getMouseSensitivity();
    }

    private ActionResult onGetFov( Camera camera, float td, boolean changing )
    {
        if ( this.keyBinding.isPressed() )
        {
            EventFields.FOV_ZOOM_LEVEL = this.zoomLevel;

            Xenon.INSTANCE.client.options.getMouseSensitivity()
                    .setValue( this.originalMouseSensitivity / this.zoomLevel );
        }
        else
        {
            EventFields.FOV_ZOOM_LEVEL = 1d;
            this.zoomLevel = 2d;

            this.originalMouseSensitivity = Xenon.INSTANCE.client.options.getMouseSensitivity().getValue();

            Xenon.INSTANCE.client.options.getMouseSensitivity()
                    .setValue( this.originalMouseSensitivity );
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
        // if our key is pressed, set the zoom level
        if ( this.keyBinding.isPressed() )
        {
            // change out zoom level based on mouse scroll
            if ( vertical > 0 ) this.zoomLevel *= 1.1;
            else if ( vertical < 0 ) this.zoomLevel *= 0.9;

            // clamp it down
            this.zoomLevel = MathHelper.clamp( this.zoomLevel, 2d, 50d );
        }

        // pass
        return ActionResult.PASS;
    }

    @Override
    protected void onEnable()
    {
    }
}
