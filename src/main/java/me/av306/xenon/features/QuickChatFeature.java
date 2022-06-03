package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.QuickChatGroup;
import me.av306.xenon.feature.IFeature;
import net.minecraft.text.TranslatableText;

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
    public void parseConfigChange( String config, String value )
    {
        if ( !config.contains( "message" ) && !config.contains( "msg" ) )
        {
            Xenon.INSTANCE.sendErrorMessage(
                    new TranslatableText(
                            "text.xenon.commandprocessor.configchange.invalidconfig",
                            this.name,
                            config,
                            value
                    )
            );

            return;
        }

        QuickChatGroup.message = value;

        Xenon.INSTANCE.sendInfoMessage(
                new TranslatableText(
                        "text.xenon.commandprocessor.configchange.success",
                        this.name,
                        config,
                        value
                )
        );
    }
}
