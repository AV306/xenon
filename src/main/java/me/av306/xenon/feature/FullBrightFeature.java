package me.av306.xenon.feature;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.template.IToggleableFeature;


public class FullBrightFeature extends IToggleableFeature
{
    public FullBrightFeature() { super.setName( "Fullbright" ); }
	
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
