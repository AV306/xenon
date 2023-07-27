package me.av306.xenon.features.render;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IToggleableFeature;
import me.av306.xenon.mixin.GameOptionsAccessor;
import me.av306.xenon.mixinterface.SimpleOptionAccessor;

public class FullBrightFeature extends IToggleableFeature
{
    public FullBrightFeature()
    {
        super( "Fullbright", "fb", "gamma" );
    }
	
    @Override
    protected void onEnable()
    {
        GameOptionsAccessor goa = ((GameOptionsAccessor) Xenon.INSTANCE.client.options);
        @SuppressWarnings( "unchecked" )
        SimpleOptionAccessor<Double> soa = ((SimpleOptionAccessor<Double>) (Object) goa.getGamma());
        soa.forceSetValue( 100.0D );
    }


    @Override
    protected void onDisable()
    {
        GameOptionsAccessor goa = ((GameOptionsAccessor) Xenon.INSTANCE.client.options);
        goa.getGamma().setValue( 1.0D );
    }
}
