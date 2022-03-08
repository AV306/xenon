package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IToggleableFeature;


public class FullBrightFeature extends IToggleableFeature
{	
    public FullBrightFeature() { super( "Fullbright" ); }
	
    @Override
    public void onEnable()
    {
        Xenon.INSTANCE.client.options.gamma = 100.0d;
    }


    @Override
    public void onDisable()
    {
        Xenon.INSTANCE.client.options.gamma = 1.0d;
    }
}
