package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.features.interfaces.IToggleableFeature;
import net.minecraft.text.LiteralText;


public class NoFireOverlayFeature implements IToggleableFeature
{
    public static boolean isEnabled = false;

    private static final String NAME = "NoFireOverlayFeature";


    @Override
    public void onEnable()
    {
        isEnabled = true;
    }


    @Override
    public void onDisable()
    {
        isEnabled = false;
    }


    @Override
    public void toggle()
    {
        if ( isEnabled ) onDisable();
        else onEnable();

        Xenon.INSTANCE.CLIENT.player.sendMessage(
                new LiteralText(
                        NAME + " " + (isEnabled ? "ENABLED" : "DISABLED") + "!"
                ),
                true
        );
    }
}
