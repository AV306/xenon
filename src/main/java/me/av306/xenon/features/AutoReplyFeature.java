package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.feature.IFeature;


public class AutoReplyFeature extends IFeature
{
	private static String message = ":";
	public static void setMessage( String newMessage) { message = newMessage; }

	public AutoReplyFeature()
	{
		super( "AutoReply" );

		// read configs
		this.message = Xenon.INSTANCE.configManager.settings.get( "autoreply.message" );
		if ( this.message == null ) this.message = "";
  }
	
    @Override
    public void onEnable()
    {
        assert Xenon.INSTANCE.client.player != null;
        Xenon.INSTANCE.client.player.sendChatMessage( message );
    }


    @Override
    public void onDisable() {}
}
