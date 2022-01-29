package me.av306.xenon.feature;

import me.av306.xenon.feature.template.IToggleableFeature;


public class NoFireOverlayFeature extends IToggleableFeature
{
    public NoFireOverlayFeature() { super.setName( "NoFireOverlay" ); }

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
