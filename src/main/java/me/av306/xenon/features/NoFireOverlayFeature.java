package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.features.interfaces.IToggleableFeature;
import net.minecraft.text.LiteralText;


public class NoFireOverlayFeature implements IToggleableFeature
{
    public static boolean isEnabled = false;


    @Override
    public void onEnable()
    {
        isEnabled = true;
        Xenon.INSTANCE.CLIENT.player.sendMessage( new LiteralText( "WARNING: Disables ClickGui!" ), true );
    }


    @Override
    public void onDisable()
    {
        isEnabled = true;
    }


    @Override
    public void toggle()
    {
        if ( isEnabled ) onDisable();
        else onEnable();
    }
}
