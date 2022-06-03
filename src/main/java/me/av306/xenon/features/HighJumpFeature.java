package me.av306.xenon.features;

import me.av306.xenon.config.GeneralConfigGroup;
import me.av306.xenon.config.feature.HighJumpGroup;
import me.av306.xenon.event.EventFields;
import me.av306.xenon.event.GetJumpVelocityEvent;
import me.av306.xenon.event.RenderInGameHudEvent;
import me.av306.xenon.feature.IToggleableFeature;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

public class HighJumpFeature extends IToggleableFeature
{
    //protected static HighJumpFeature instance;

    private short ticks = 0;

    public HighJumpFeature()
    {
        super( "HighJump" );

        RenderInGameHudEvent.AFTER_VIGNETTE.register( this::onRenderInGameHud );
        GetJumpVelocityEvent.EVENT.register( this::onGetJumpVelocity );

        //instance = this;
    }

    private ActionResult onRenderInGameHud( MatrixStack matrixStack, float tickDelta )
    {
        if ( !this.isEnabled ) return ActionResult.PASS;

        if ( ticks >= GeneralConfigGroup.interval )
        {
            ticks = 0;
            this.setName( "HighJump (" + HighJumpGroup.height + ")" );
        }
        else ticks++;

        return ActionResult.PASS;
    }

    private ActionResult onGetJumpVelocity()
    {
        if ( !this.isEnabled ) return ActionResult.PASS;

        float height = HighJumpGroup.height * HighJumpGroup.multiplier;
        EventFields.JUMP_VELOCITY_MODIFIER = height;

        return ActionResult.PASS;
    }

    /*@Override
    public static HighJumpFeature getInstance()
    {
        return instance;
    }*/

    @Override
    protected void onEnable()
    {
        float height = HighJumpGroup.height * HighJumpGroup.multiplier;
        EventFields.JUMP_VELOCITY_MODIFIER = height;
        this.setName( "HighJump (" + height + ")" );
    }

    @Override
    protected void onDisable()
    {
        EventFields.JUMP_VELOCITY_MODIFIER = 0f; // TODO: make way for other features that might want to modify this
    }
}
