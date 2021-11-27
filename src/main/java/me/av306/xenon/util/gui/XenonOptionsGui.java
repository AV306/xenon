package me.av306.xenon.util.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class XenonOptionsGui extends LightweightGuiDescription
{
    public XenonOptionsGui()
    {
        WGridPanel root = new WGridPanel();
        setRootPanel( root );
        root.setSize( 256, 240 );
        root.setInsets( Insets.ROOT_PANEL );

        WSprite icon = new WSprite( new Identifier( "minecraft:textures/item/redstone.png" ) );
        root.add(icon, 0, 2, 1, 1);

        WButton button = new WButton( new TranslatableText( "gui.xenon.examplebutton" ) );
        root.add(button, 0, 3, 4, 1);

        WLabel label = new WLabel( new LiteralText( "Test" ), 0xFFFFFF );
        root.add( label, 0, 4, 2, 1 );

        root.validate( this );
    }
}
