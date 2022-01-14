package me.av306.xenon.commands;

import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;

public class CommandHandler
{
	//private HashMap<

	
	public CommandHandler() {}

	//public void addCommand( String name, , IFeature)


	public void registerDefaultCommands() 
	{
		ClientCommandManager.DISPATCHER.register(
			ClientCommandManager.literal( "test" )
			.executes( context ->
			{
				context.getSource().sendFeedback( "Client command test!" );
			})
		);
	}
}