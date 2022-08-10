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
    private int zoomLevel = 2;

    public ZoomFeature()
    {
        super( "Zoom" );

        MouseScrollEvent.EVENT.register( this::onMouseScroll );
        ScrollInHotbarEvent.EVENT.register( this::onScrollInHotbar );
    }

    private ActionResult onScrollInHotbar( double amount )
    {
        // cancel the hotbar scroll if this is enabled
        // and consume it for the zoom adjustment.
        if ( this.isEnabled ) return ActionResult.FAIL;
        else return ActionResult.PASS;
    }

    private ActionResult onMouseScroll( long window, double horizontal, double vertical )
    {
        // if our key is not pressed, pass
        if ( this.keyBinding.isPressed() )
        {

            // change out zoom level based on mouse scroll
            if (vertical > 0) this.zoomLevel *= 1.1;
            else if (vertical < 0) this.zoomLevel *= 0.9;

            // clamp it down
            this.zoomLevel = MathHelper.clamp(this.zoomLevel, 0, 50);
        }

        // pass
        return ActionResult.PASS;
    }

    @Override
    protected void keyEvent()
    {
        // TODO: Finish this bit
        if ( this.keyBinding.isPressed() )
        {
            EventFields.shouldOverrideFov = true;
            EventFields.FOV_OVERRIDE = (float) (Xenon.INSTANCE.client.options.getFov().getValue() / this.zoomLevel);
        }
        else EventFields.shouldOverrideFov = false;
    }

    @Override
    protected void onEnable()
    {

    }
}
