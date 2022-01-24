package me.av306.xenon.features.interfaces;


public abstract class IToggleableFeature extends IFeature
{
    public void toggle()
		{
			if ( super.isEnabled ) onDisable();
        else onEnable();

        Xenon.INSTANCE.CLIENT.player.sendMessage(
                new LiteralText(
                        super.name + " " + (isEnabled ? "ENABLED" : "DISABLED") + "!"
                ),
                true
        );
		}
}
