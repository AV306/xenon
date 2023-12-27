package me.av306.xenon.features.render;

import me.av306.xenon.Xenon;
import me.av306.xenon.event.EventFields;
import me.av306.xenon.event.GetFovEvent;
import me.av306.xenon.feature.IToggleableFeature;
import net.minecraft.client.render.Camera;
import net.minecraft.util.ActionResult;

public class AustralianModeFeature extends IToggleableFeature
{
    public AustralianModeFeature()
    {
        super( "Australian Mode", "aussiemode", "aumode", "aussie", "au" );

        GetFovEvent.EVENT.register( this::onGetFov );
    }

    private ActionResult onGetFov( Camera camera, float tickDelta, boolean changingFov )
    {
        if ( this.isEnabled )
            //EventFields.FOV_OVERRIDE = 360d - Xenon.INSTANCE.client.options.getFov().getValue();
            //EventFields.FOV_OVERRIDE = -Xenon.INSTANCE.client.options.getFov().getValue();
            GetFovEvent.EventData.FOV_OVERRIDE = -Xenon.INSTANCE.client.options.getFov().getValue();

        return ActionResult.PASS;
    }

    @Override
    protected void onEnable()
    {
        //EventFields.shouldOverrideFov = true;
        GetFovEvent.EventData.SHOULD_OVERRIDE_FOV = true;
    }

    @Override
    protected void onDisable()
    {
        //EventFields.shouldOverrideFov = false;
        GetFovEvent.EventData.SHOULD_OVERRIDE_FOV = false;
    }
}
