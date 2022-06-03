package me.av306.xenon.features;

import me.av306.xenon.config.GeneralConfigGroup;
import me.av306.xenon.config.feature.ExtraReachGroup;
import me.av306.xenon.event.EventFields;
import me.av306.xenon.event.GetReachDistanceEvent;
import me.av306.xenon.event.RenderInGameHudEvent;
import me.av306.xenon.feature.IToggleableFeature;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

public class ExtraReachFeature extends IToggleableFeature
{
    private short ticks = 0;

    public ExtraReachFeature()
    {
        super( "ExtraReach" );

        GetReachDistanceEvent.EVENT.register( this::onGetReachDistance );
        RenderInGameHudEvent.AFTER_VIGNETTE.register( this::onRenderInGameHud );
    }

    private ActionResult onRenderInGameHud( MatrixStack matrixStack, float v )
    {
        if ( !this.isEnabled ) return ActionResult.PASS;

        if ( ticks >= GeneralConfigGroup.interval )
        {
            ticks = 0;
            this.setName( "ExtraReach (" + ExtraReachGroup.extraReach + ")" );
        }
        else ticks++;

        return ActionResult.PASS;
    }

    private ActionResult onGetReachDistance()
    {
        if ( !this.isEnabled ) return ActionResult.PASS;

        EventFields.REACH_MODIFIER = ExtraReachGroup.extraReach;

        return ActionResult.PASS;
    }

    @Override
    protected void onEnable()
    {
        this.setName( "ExtraReach (" + ExtraReachGroup.extraReach + ")" );
    }

    @Override
    protected void onDisable()
    {
        EventFields.REACH_MODIFIER = 0f;
    }
}
