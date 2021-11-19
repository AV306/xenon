package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.features.interfaces.IToggleableFeature;
import net.minecraft.text.LiteralText;

public class TestToggleableFeature implements IToggleableFeature
{
    public static boolean isEnabled = false;


    @Override
    public void onEnable()
    {
        isEnabled = true;
        Xenon.INSTANCE.CLIENT.player.setInvisible( true );
        Xenon.INSTANCE.CLIENT.player.setInvulnerable( true );
        Xenon.INSTANCE.CLIENT.player.sendMessage( new LiteralText( "Test toggleable Feature ENABLED!" ), true );
    }

    @Override
    public void onDisable()
    {
        isEnabled = false;
        Xenon.INSTANCE.CLIENT.player.setInvisible( false );
        Xenon.INSTANCE.CLIENT.player.setInvulnerable( false );
        Xenon.INSTANCE.CLIENT.player.sendMessage( new LiteralText( "Test Toggleable Feature DISABLED!" ), true );
    }

    @Override
    public void toggle()
    {
        if ( isEnabled ) onDisable();
        else onEnable();
    }
}
