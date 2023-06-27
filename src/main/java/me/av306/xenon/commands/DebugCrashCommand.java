package me.av306.xenon.commands;

import me.av306.xenon.Xenon;
import me.av306.xenon.command.Command;

public class DebugCrashCommand extends Command
{
	public DebugCrashCommand() { super( "debugcrash", "dbgcrash", "crash" ); }

	@Override
	public void execute( String[] args )
	{
		Xenon.INSTANCE.LOGGER.warn( "The following out-of-bounds access is intentional." );
		// Do an out-of-bounds access to crash
		args[args.length] = "";

		//if ( args[0].equals( "confirm" ) )
		//	GlfwUtil.makeJvmCrash(); // DOn't use this because it won't save the world
		//else Xenon.INSTANCE.sendWarningMessage( "text.xenon.command.debugcrash.warning" );
	}
}
