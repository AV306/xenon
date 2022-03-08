package me.av306.xenon.features;

import me.av306.xenon.feature.IToggleableFeature;


public class NoFireOverlayFeature extends IToggleableFeature
{
    public NoFireOverlayFeature() { super( "NoFireOverlay" ); }

	// handled in InGameHudMixin
	
    @Override
    public void onEnable()
    {
    }


    @Override
    public void onDisable()
    {
    }
}
