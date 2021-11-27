package me.av306.xenon.util.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.text.Text;

public class XenonGuiScreen extends CottonClientScreen
{
    public XenonGuiScreen( Text title, GuiDescription description )
    {
        super( title, description );
    }


    public XenonGuiScreen( GuiDescription description )
    {
        super(description);
    }
}
