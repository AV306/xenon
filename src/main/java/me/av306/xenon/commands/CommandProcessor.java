package me.av306.xenon.commands;

import me.av306.xenon.Xenon;
import me.av306.xenon.features.*;

public class CommandProcessor
{
	// this is just a basic command processor.
	public void execute( String message )
	{
		String[] command = message.split(" ");

		switch ( command[0] )
		{
			case "autoreply":
				AutoReplyFeature.processCommand( command );
				break;

			case "fullbright":
				break;

			default:
				Xenon.INSTANCE.CLIENT.player.sendMessage( new LiteralText( "[Xenon] ERROR: Not a valid command!" ) );
		}
}
