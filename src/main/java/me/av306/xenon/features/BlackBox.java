package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.BlackBoxGroup;
import me.av306.xenon.config.feature.FeatureListGroup;
import me.av306.xenon.event.MinecraftClientEvents;
import me.av306.xenon.feature.IToggleableFeature;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.util.ActionResult;

import java.io.*;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;

public class BlackBox extends IToggleableFeature
{
	private File logFile = null;
	private OutputStreamWriter logFileOutput = null;
	private int index = 0;

	private final Calendar calendar;

	public BlackBox()
	{
		super( "BlackBox" );

		//this.setShouldHide( BlackBoxGroup.showInFeatureList );
		this.calendar = Calendar.getInstance();
	}

	@Override
	protected void onEnable()
	{
		// Open a new log file and output stream
		try
		{
			String timestamp = this.calendar.getTime()
					.toString()
					.replace( ':', '.' ); // Windows doesn't like colons
			String gameDir = FabricLoader.getInstance().getGameDir().toString();

			//Xenon.INSTANCE.LOGGER.info( gameDir + File.separator + timestamp + this.index + ".log" );

			this.logFile = new File( gameDir + File.separator + timestamp + this.index + ".log" );
			this.logFile.createNewFile();

			this.logFileOutput = new OutputStreamWriter( new FileOutputStream( this.logFile ) );
			this.logFileOutput.append( timestamp ).append( "BlackBox logging started. Stay safe!\n" );

			// Write world name/server ip
			ServerInfo serverInfo = Xenon.INSTANCE.client.getNetworkHandler().getServerInfo();

			// FIXME: everything is null somehow and idfk why

			if ( serverInfo == null )
			{
				this.writeData( "Server info was null; something really bad happened and BlackBox can't continue logging. Sorry!" );
				this.disable();
			}

			if ( !serverInfo.isLocal() )
			{
				this.writeData("Server address: ", serverInfo.address);
				this.writeData("Number of players: ", String.valueOf(serverInfo.players.online()));
			}
			else this.writeData( "Local server" );

			assert Xenon.INSTANCE.client.interactionManager != null;
			this.writeData( "Gamemode: ", Xenon.INSTANCE.client.interactionManager.getCurrentGameMode().getName() );
		}
		catch ( IOException ioe )
		{
			Xenon.INSTANCE.sendErrorMessage( "text.xenon.blackbox.ioexception.creation" );
			ioe.printStackTrace();
			this.logFile = null;
			this.logFileOutput = null;
		}
	}

	@Override
	protected void onDisable()
	{
		this.cleanup( true );

		//Xenon.INSTANCE.LOGGER.info( "index: {}", this.index );
	}

	private void writeData( String... datas ) throws IOException
	{
		if ( this.logFileOutput == null ) return;

		this.logFileOutput.append( '[' )
				.append( this.calendar.getTime().toString() )
				.append( "] " );

		for ( String data : datas ) this.logFileOutput.append( data );

		this.logFileOutput.append( '\n' );
	}

	private void cleanup()
	{
		if ( this.logFile != null && this.logFileOutput != null )
		{
			// Clean up gracefully
			try { logFileOutput.close(); }
			catch ( IOException e ) { Xenon.INSTANCE.sendErrorMessage( "text.xenon.blackbox.ioexception.disposal" ); }

			this.logFile = null;
			this.logFileOutput = null;
			index++;
		}
	}
}
