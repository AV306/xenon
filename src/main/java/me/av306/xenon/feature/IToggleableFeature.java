package me.av306.xenon.feature.template;

import me.av306.xenon.Xenon;
import net.minecraft.text.LiteralText;

public abstract class IToggleableFeature extends IFeature
{
	protected IToggleableFeature( String name ) { super( name ); }
	protected IToggleableFeature() { this( "IToggleableFeature" ); }

  public void toggle()
	{
		if ( this.isEnabled ) disable();
        else enable();

        Xenon.INSTANCE.client.player.sendMessage(
                new LiteralText(
                        this.name() + " " + (super.isEnabled ? "ENABLED" : "DISABLED") + "!"
                ),
                true
        );
	}
}
