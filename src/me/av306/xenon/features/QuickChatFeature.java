package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.QuickChatGroup;
import me.av306.xenon.feature.IFeature;

public class QuickChatFeature extends IFeature
{
	public QuickChatFeature()
	{
		super( "QuickChat" );
  }
	
    @Override
    public void onEnable()
    {
        assert Xenon.INSTANCE.client.player != null;

        // field will be updated every time configs are changed
        Xenon.INSTANCE.client.player.sendChatMessage( QuickChatGroup.message );
    }

    @Override
    public boolean onConfigChange( String config, String value )
    {
        boolean result = config.contains( "message" ) || config.contains( "msg" );

        if ( result ) QuickChatGroup.message = value;

        return result;
    }
}
