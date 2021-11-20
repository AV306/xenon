package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.features.interfaces.IFeature;


public class AutoReplyFeature implements IFeature
{
    @Override
    public void onEnable()
    {
        Xenon.INSTANCE.CLIENT.player.sendChatMessage( ":" );
    }


    @Override
    public void onDisable() {}
}
