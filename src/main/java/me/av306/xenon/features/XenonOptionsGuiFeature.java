package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.features.interfaces.IFeature;
import me.av306.xenon.util.gui.XenonOptionsGui;
import me.av306.xenon.util.gui.XenonCottonClientGuiScreen;

public class XenonOptionsGuiFeature extends IFeature
{
    @Override
    public void onEnable()
    {
        Xenon.INSTANCE.CLIENT.setScreen( new XenonCottonClientGuiScreen( new XenonOptionsGui() ) );
    }

    @Override
    public void onDisable() {}
}
