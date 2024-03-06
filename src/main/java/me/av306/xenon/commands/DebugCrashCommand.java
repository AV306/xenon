package me.av306.xenon.commands;

import me.av306.xenon.Xenon;
import me.av306.xenon.command.Command;
import net.minecraft.client.Keyboard;
import net.minecraft.client.util.GlfwUtil;

public class DebugCrashCommand extends Command
{
	public DebugCrashCommand() { super( "debugcrash", "dbgcrash", "crash" ); }

	@Override
	public void execute( String[] args )
	{
		//Xenon.INSTANCE.LOGGER.warn( "The following out-of-bounds access is intentional." );

		// MC catches the OOBE :(
		args[args.length] = "";

		if ( args[0].equals( "force" ) ) GlfwUtil.makeJvmCrash();
		//System.exit( -1 );

		//Xenon.INSTANCE.client.keyboard.pollDebugCrash();

		//if ( args[0].equals( "confirm" ) )
		//	GlfwUtil.makeJvmCrash(); // Don't use this because it won't save the world
		//else Xenon.INSTANCE.sendWarningMessage( "text.xenon.command.debugcrash.warning" );
	}
}
