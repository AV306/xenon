package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.features.interfaces.IToggleableFeature;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;


public class FullBrightFeature extends IToggleableFeature
{
    public FullBrightFeature() { super.setName( "Fullbright" ); }
	
    @Override
    public void onEnable()
    {
        Xenon.INSTANCE.client.options.gamma = 100.0d;
    }


    @Override
    public void onDisable()
    {
        Xenon.INSTANCE.client.options.gamma = 1.0d;
    }
}
