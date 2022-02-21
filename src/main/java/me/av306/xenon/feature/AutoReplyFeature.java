package me.av306.xenon.feature;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.template.*;


public class AutoReplyFeature extends IFeature
{
  public String name = "";
	public String message = ":";
	
    @Override
    public void onEnable()
    {
        assert Xenon.INSTANCE.client.player != null;
        Xenon.INSTANCE.client.player.sendChatMessage( message );
    }


    @Override
    public void onDisable() {}
}
