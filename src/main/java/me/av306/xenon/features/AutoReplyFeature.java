package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.AutoReplyGroup;
import me.av306.xenon.feature.IFeature;


public class AutoReplyFeature extends IFeature
{
	private String message = AutoReplyGroup.message; //

	public AutoReplyFeature()
	{
		super( "AutoReply" );
  }
	
    @Override
    public void onEnable()
    {
        assert Xenon.INSTANCE.client.player != null;
        Xenon.INSTANCE.client.player.sendChatMessage( message );
    }
}
