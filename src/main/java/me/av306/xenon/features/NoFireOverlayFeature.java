package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.features.interfaces.IToggleableFeature;
import net.minecraft.text.LiteralText;


public class NoFireOverlayFeature extends IToggleableFeature
{
    public static String name = "NoFireOverlayFeature";

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
