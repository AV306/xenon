package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.AutoReplyGroup;
import me.av306.xenon.feature.IFeature;


public class AutoReplyFeature extends IFeature
{
	public AutoReplyFeature()
	{
		super( "AutoReply" );
  }
	
    @Override
    public void onEnable()
    {
        assert Xenon.INSTANCE.client.player != null;
        Xenon.INSTANCE.client.player.sendChatMessage( AutoReplyGroup.message ); // field will be updated every time configs are changed
    }
}
