package me.av306.xenon.feature;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.template.IFeature;
import me.av306.xenon.gui.XenonOptionsGui;
import me.av306.xenon.gui.XenonCottonClientGuiScreen;

public class XenonOptionsGuiFeature extends IFeature
{
    @Override
    public void onEnable()
    {
        Xenon.INSTANCE.client.setScreen( new XenonCottonClientGuiScreen( new XenonOptionsGui() ) );
    }

    @Override
    public void onDisable()
		{
			this.applySettings();
		}

	private void applySettings() {}
}
