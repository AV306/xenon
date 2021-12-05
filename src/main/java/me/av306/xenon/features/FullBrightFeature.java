package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.features.interfaces.IToggleableFeature;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;


public class FullBrightFeature implements IToggleableFeature
{
    public static boolean isEnabled = false;


    @Override
    public void onEnable()
    {
        isEnabled = true;
        Xenon.INSTANCE.CLIENT.options.gamma = 15.0d;
        Xenon.INSTANCE.CLIENT.player.sendMessage( new LiteralText( "Fullbright ENABLED!" ), true );
    }


    @Override
    public void onDisable()
    {
        isEnabled = false;
        Xenon.INSTANCE.CLIENT.options.gamma = 1.0d;
        Xenon.INSTANCE.CLIENT.player.sendMessage( new LiteralText( "Fullbright DISABLED!" ), true );
    }


    @Override
    public void toggle()
    {
        if ( isEnabled ) onDisable();
        else onEnable();
    }
}
