package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.mixin.GameOptionsAccessor;

public class FullBrightFeature extends IToggleableFeature
{
    public FullBrightFeature()
    {
        super( "Fullbright" );
    }
	
    @Override
    protected void onEnable()
    {
        ((GameOptionsAccessor) Xenon.INSTANCE.client.options).getGamma().setValue( 100.0D );
    }


    @Override
    protected void onDisable()
    {
        ((GameOptionsAccessor) Xenon.INSTANCE.client.options).getGamma().setValue( 1.0D );
    }
}
