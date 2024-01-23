package me.av306.xenon.command;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

import com.mojang.brigadier.arguments.StringArgumentType;

import me.av306.xenon.Xenon;
import me.av306.xenon.util.text.TextFactory;
import net.minecraft.text.Text;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;

public abstract class Command
{
	protected final String name;
	public String getName() { return this.name; }

	public Command( String name )
	{
		this.name = name;
		Xenon.INSTANCE.commandRegistry.put( this.name, this );

		ClientCommandRegistrationCallback.EVENT.register(
			(dispatcher, registryAccess) -> dispatcher.register(
				ClientCommandManager.literal( name )
						.executes( context ->
						{
							this.execute( null );
							return 1;
						} )
						/*.then( argument( "arguments", StringArgumentType.greedyString() ))
						.executes( context -> 
						{
							//context.getSource().sendFeedback( TextFactory.createLiteral( "Executed command for " + name ) );
							this.execute( StringArgumentType.getString( context, "arguments" ).split( " " ) );
							return 1;
						} )*/ // FIXME: I really have no idea why string arguments don't work
			)
		);
	}

	public Command( String name, String... aliases )
	{
		this( name );
		
		for ( String alias : aliases )
		{
			Xenon.INSTANCE.commandRegistry.put( alias, this );
			ClientCommandRegistrationCallback.EVENT.register(
				(dispatcher, registryAccess) -> dispatcher.register(
					ClientCommandManager.literal( alias )
						.executes( context ->
						{
							this.execute( null );
							return 1;
						} )

						/*.then( argument( "args", StringArgumentType.greedyString() ))
						.executes( context -> 
						{
							//context.getSource().sendFeedback( TextFactory.createLiteral( "Executed command for " + name ) );
							this.execute( StringArgumentType.getString( context, "args" ).split( " " ) );
							return 1;
						} )*/
				)
			);
		}
	}

	/**
	 * Implementations must handle empty or null arguments, to allow customisable default arguments
	 */
	public abstract void execute( String[] args );

	protected void sendInfoMessage( String key, Object... args )
	{
		Text message = Xenon.INSTANCE.getNamePrefixCopy().append(
				TextFactory.createTranslatable(
						"text.xenon.message",
						this.name,
						TextFactory.createTranslatable( key, args )
				)
		);

		try
		{
			Xenon.INSTANCE.client.player.sendMessage( message );
		}
		catch ( NullPointerException ignored ) {}
	}

	protected void sendErrorMessage( String key, Object... args )
	{
		Text message = Xenon.INSTANCE.getNamePrefixCopy().append(
				TextFactory.createTranslatable(
						"text.xenon.message",
						this.name,
						TextFactory.createTranslatable( key, args ).formatted( Xenon.INSTANCE.ERROR_FORMAT )
				)
		);

		try
		{
			Xenon.INSTANCE.client.player.sendMessage( message );
		}
		catch ( NullPointerException ignored ) {}
	}
}
