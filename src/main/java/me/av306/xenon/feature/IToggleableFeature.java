package me.av306.xenon.feature;

import me.av306.xenon.Xenon;
import net.minecraft.text.LiteralText;

public abstract class IToggleableFeature extends IFeature
{
	protected IToggleableFeature( String name ) { super( name ); }
	protected IToggleableFeature() { this( "IToggleableFeature" ); }

    @Override
    public void enable()
    {
        if ( isEnabled ) return; // safety

        isEnabled = true;

        Xenon.INSTANCE.enabledFeatures.add( this );

        onEnable();
    }

    @Override
    public void disable()
    {
        if ( !isEnabled ) return;

        isEnabled = false;

        Xenon.INSTANCE.enabledFeatures.remove( this );

        onDisable();
    }

    public void toggle()
    {
      if ( isEnabled ) disable();
        else enable();

      Xenon.INSTANCE.client.player.sendMessage(
                new LiteralText(
                        name + " " + (isEnabled ? "ENABLED" : "DISABLED") + "!"
                ).formatted( Xenon.INSTANCE.SUCCESS_FORMAT ),
                true
      );
    }
}
