package me.av306.xenon.features.render;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.EventFields;
import me.av306.xenon.event.MouseScrollEvent;
import me.av306.xenon.event.ScrollInHotbarEvent;
import me.av306.xenon.feature.IFeature;
import net.minecraft.util.ActionResult;

public class ZoomFeature extends IFeature
{
    private int zoomLevel = 1;

    public ZoomFeature()
    {
        super( "Zoom" );

        MouseScrollEvent.EVENT.register( this::onMouseScroll );
        ScrollInHotbarEvent.EVENT.register( this::onScrollInHotbar );
    }

    private ActionResult onScrollInHotbar( double amount )
    {
        if ( this.isEnabled ) return ActionResult.FAIL;
    }

    private ActionResult onMouseScroll( long window, double horizontal, double vertical )
    {
        return ActionResult.PASS;
    }

    @Override
    protected void keyEvent()
    {
        if ( this.keyBinding.isPressed() )
        {
            EventFields.FOV_OVERRIDE = (float) (Xenon.INSTANCE.client.options.getFov().getValue() / this.zoomLevel);
        }
    }

    @Override
    protected void onEnable()
    {

    }
}
