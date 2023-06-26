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
import java.time.LocalDate;
import java.time.LocalTime;

public class BlackBox extends IToggleableFeature
{
	private File logFile = null;
	private OutputStreamWriter logFileOutput = null;
	private int index = 0;

	public BlackBox()
	{
		super( "BlackBox" );

		//this.setShouldHide( BlackBoxGroup.showInFeatureList );
	}

	@Override
	protected void onEnable()
	{
		// Open a new log file and output stream
		this.createNewFile();

		// Write starting data
	}

	@Override
	protected void onDisable()
	{
		this.cleanup();

		//Xenon.INSTANCE.LOGGER.info( "index: {}", this.index );
	}

	private void createLogFile()
	{
		try
		{
			String datestamp = LocalDate.now().toString();
			String gameDir = FabricLoader.getInstance().getGameDir().toString();

			//Xenon.INSTANCE.LOGGER.info( gameDir + File.separator + timestamp + this.index + ".log" );

			this.logFile = new File( gameDir + File.separator + datestamp + "-" + this.index + ".data" );
			this.logFile.createNewFile();

			this.logFileOutput = new OutputStreamWriter( new FileOutputStream( this.logFile ) );
			this.logFileOutput.append( LocalTime.now().toString() ).append( "BlackBox logging started. Stay safe!\n" );

		}
		catch ( IOException ioe )
		{
			Xenon.INSTANCE.sendErrorMessage( "text.xenon.blackbox.ioexception.creation" );
			ioe.printStackTrace();
			this.logFile = null;
			this.logFileOutput = null;
			this.disable();
		}
	}

	private void writeData( String... datas )
	{
		try
		{
			this.logFileOutput.append( '[' )
					.append( LocalTime.now().toString() )
					.append( "] " );

			for ( String data : datas ) this.logFileOutput.append( data );

			this.logFileOutput.append( '\n' );
		}
		catch ( IOException ioe )
		{
			ioe.printStackTrace();
			Xenon.INSTANCE.sendErrorMessage( "text.xenon.blackbox.ioexception.write" );
		}
		catch ( NullPointerException npe )
		{
			Xenon.INSTANCE.LOGGER.warn( "BlackBox write attempted but logfile was null!" );
		}
	}

	private void cleanup()
	{
		if ( this.logFile == null || this.logFileOutput == null ) return;


		// Clean up gracefully
		try { this.writeData( "BlackBox logging stopped." ); logFileOutput.close(); }
		catch ( IOException e ) { Xenon.INSTANCE.sendErrorMessage( "text.xenon.blackbox.ioexception.disposal" ); }

		this.logFile = null;
		this.logFileOutput = null;
		index++;
	}
}
