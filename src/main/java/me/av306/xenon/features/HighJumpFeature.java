package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.HighJumpGroup;
import me.av306.xenon.event.EventFields;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.mixin.ClientPlayerEntityMixin;

public class HighJumpFeature extends IToggleableFeature
{
    //protected static HighJumpFeature instance;

    public HighJumpFeature()
    {
        super( "HighJump" );

        //instance = this;
    }

    /*@Override
    public static HighJumpFeature getInstance()
    {
        return instance;
    }*/

    @Override
    protected void onEnable()
    {
        EventFields.JUMP_VELOCITY_MODIFIER += HighJumpGroup.height;
    }

    @Override
    protected void onDisable()
    {
        EventFields.JUMP_VELOCITY_MODIFIER -= HighJumpGroup.height;
    }
}
