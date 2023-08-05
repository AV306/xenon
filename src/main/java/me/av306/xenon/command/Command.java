package me.av306.xenon.command;

import me.av306.xenon.Xenon;
import me.av306.xenon.util.text.TextFactory;
import net.minecraft.text.Text;

public abstract class Command
{
	protected final String name;
	public String getName() { return this.name; }

	public Command( String name )
	{
		this.name = name;
		Xenon.INSTANCE.commandRegistry.put( this.name, this );
	}

	public Command( String name, String... aliases )
	{
		this( name );
		
		for ( String alias : aliases )
			Xenon.INSTANCE.commandRegistry.put( alias, this );
	}

	/**
	 * Implementations must handle empty arguments
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
