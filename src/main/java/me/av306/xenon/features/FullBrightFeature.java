package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IToggleableFeature;


public class FullBrightFeature extends IToggleableFeature
{
    public FullBrightFeature() { super( "Fullbright" ); }
	
    @Override
    protected void onEnable()
    {
        Xenon.INSTANCE.client.options.gamma = 100.0d;
    }


    @Override
    protected void onDisable()
    {
        Xenon.INSTANCE.client.options.gamma = 1.0d;
    }

    @Override
    public void parseConfigChange( String config, String value ) {}
}
