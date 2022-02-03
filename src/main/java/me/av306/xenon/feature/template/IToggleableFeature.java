package me.av306.xenon.feature.template;

import me.av306.xenon.Xenon;
import net.minecraft.text.LiteralText;

public abstract class IToggleableFeature extends IFeature
{
	public String name = "IToggleableFeature";
	public IToggleableFeature() { super.setName( name ); }

	
  public void toggle()
	{
		if ( super.isEnabled ) disable();
        else enable();

        Xenon.INSTANCE.client.player.sendMessage(
                new LiteralText(
                        name + " " + (super.isEnabled ? "ENABLED" : "DISABLED") + "!"
                ),
                true
        );
	}
}
