package me.av306.xenon.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import me.av306.xenon.Xenon;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class XenonOptionsGui extends LightweightGuiDescription
{
    public XenonOptionsGui()
    {
        WGridPanel root = new WGridPanel();
        setRootPanel( root );
        root.setSize( 30, 30 );
        root.setInsets( Insets.ROOT_PANEL );

        WTextField searchbar = new WTextField();
        root.add( searchbar, 0, 0, 50, 1 );

        WLabel label = new WLabel( new LiteralText( "Test" ), 0xFFFFFF );
        root.add( label, 0, 4, 2, 1 );

        root.validate( this );
    }
}
