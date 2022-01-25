package me.av306.xenon.features.interfaces;

import me.av306.xenon.Xenon;
import net.minecraft.text.LiteralText;

public abstract class IToggleableFeature extends IFeature
{
    public void toggle()
		{
			if ( super.isEnabled ) onDisable();
        else onEnable();

        Xenon.INSTANCE.client.player.sendMessage(
                new LiteralText(
                        super.name + " " + (isEnabled ? "ENABLED" : "DISABLED") + "!"
                ),
                true
        );
		}
}
