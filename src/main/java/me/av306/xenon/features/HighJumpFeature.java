package me.av306.xenon.features;

import me.av306.xenon.Xenon;
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
    }

    @Override
    protected void onDisable()
    {
    }
}
