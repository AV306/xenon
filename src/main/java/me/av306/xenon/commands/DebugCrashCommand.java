package me.av306.xenon.commands;

import me.av306.xenon.Xenon;
import me.av306.xenon.command.Command;

import net.minecraft.client.util.GlfwUtil;

public class DebugCrashCommand extends Command
{
	public DebugCrashCommand() { super( "debugcrash", "dbgcrash", "crash" ); }

	@Override
	public void execute( String[] args )
	{
		GlfwUtil.makeJvmCrash();
	}
}