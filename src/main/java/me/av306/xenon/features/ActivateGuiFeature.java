package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.features.interfaces.IFeature;
import me.av306.xenon.gui.XenonGui;
import me.av306.xenon.gui.XenonGuiScreen;

public class ActivateGuiFeature implements IFeature
{
    @Override
    public void onEnable()
    {
        Xenon.INSTANCE.CLIENT.setScreen( new XenonGuiScreen( new XenonGui() ) );
    }

    @Override
    public void onDisable() {}
}
