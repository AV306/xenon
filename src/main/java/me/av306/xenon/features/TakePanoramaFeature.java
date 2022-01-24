package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.features.interfaces.IFeature;
import net.minecraft.text.Text;

import java.io.File;

public class TakePanoramaFeature extends IFeature
{
    @Override
    public void onEnable()
    {
        Text text = Xenon.INSTANCE.CLIENT.takePanorama( Xenon.INSTANCE.CLIENT.runDirectory, 1024, 1024 );

        Xenon.INSTANCE.CLIENT.player.sendMessage( text, false );
    }

    @Override
    public void onDisable() {}
}
