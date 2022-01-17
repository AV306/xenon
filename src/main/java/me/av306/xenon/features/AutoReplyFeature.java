package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.features.interfaces.*;


public class AutoReplyFeature implements IFeature, ICommand
{
		private static String message = ""; // FIXME: optimize this, stop using so much static shit
	
    @Override
    public void onEnable()
    {
        Xenon.INSTANCE.CLIENT.player.sendChatMessage( message );
    }


    @Override
    public void onDisable() {}


		@Override // FIXME
		public static void processCommand( String[] command )
		{
			message = command[1];
		}
}
