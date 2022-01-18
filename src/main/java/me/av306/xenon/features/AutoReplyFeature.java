package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.features.interfaces.*;


public class AutoReplyFeature implements IFeature
{
	public static String message = ""; // FIXME: optimize this, stop using so much static shit
	
    @Override
    public void onEnable()
    {
        assert Xenon.INSTANCE.CLIENT.player != null;
        Xenon.INSTANCE.CLIENT.player.sendChatMessage( message );
    }


    @Override
    public void onDisable() {}
}
