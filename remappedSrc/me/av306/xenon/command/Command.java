package me.av306.xenon.command;

import me.av306.xenon.Xenon;

public abstract class Command
{
	protected final String name;
	public String getName() { return this.name; }

	public Command( String name )
	{
		this.name = name;
		Xenon.INSTANCE.commandRegistry.put( this.name, this );
	}

	public abstract void execute( String[] args );
}
