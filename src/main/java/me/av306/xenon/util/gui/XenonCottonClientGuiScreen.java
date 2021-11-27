package me.av306.xenon.util.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.text.Text;

public class XenonCottonClientGuiScreen extends CottonClientScreen
{
    public XenonCottonClientGuiScreen(Text title, GuiDescription description )
    {
        super( title, description );
    }


    public XenonCottonClientGuiScreen( GuiDescription description )
    {
        super( description );
    }
}
