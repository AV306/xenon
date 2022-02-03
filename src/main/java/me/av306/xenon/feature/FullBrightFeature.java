package me.av306.xenon.feature;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.template.IToggleableFeature;


public class FullBrightFeature extends IToggleableFeature
{
		public String name = "Fullbright";
	
    public FullBrightFeature() { super.setName( name ); }
	
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
