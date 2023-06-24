package me.av306.xenon.features;

import me.av306.xenon.Xenon;
import me.av306.xenon.config.feature.BlackBoxGroup;
import me.av306.xenon.feature.IToggleableFeature;
import net.fabricmc.loader.api.FabricLoader;

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
			String timestamp = this.getTimeStamp().replace( ':', '.' );
			String gameDir = FabricLoader.getInstance().getGameDir().toString();
			Xenon.INSTANCE.LOGGER.info( gameDir + File.separator + timestamp + this.index + ".log" );
			this.logFile = new File( gameDir + File.separator + timestamp + this.index + ".log" );
			this.logFile.createNewFile();
			this.logFileOutput = new OutputStreamWriter( new FileOutputStream( this.logFile ) );
			this.logFileOutput.append( timestamp + "BlackBox logging started. Stay safe!" );
		}
		catch ( IOException ioe )
		{
			Xenon.INSTANCE.sendErrorMessage( "text.xenon.blackbox.ioexception.creation" );
			ioe.printStackTrace();
			this.logFile = null;
			this.logFileOutput = null;
			this.disable(); // TODO: check for double close
		}
	}

	@Override
	protected void onDisable()
	{
		if ( this.logFile != null && this.logFileOutput != null )
		{
			// Clean up gracefully
			try { logFileOutput.close(); }
			catch ( IOException e ) { Xenon.INSTANCE.sendErrorMessage( "text.xenon.blackbox.ioexception.disposal" ); }

			this.logFile = null;
			this.logFileOutput = null;
			this.index++;
		}

		// Debug
		Xenon.INSTANCE.LOGGER.info( "index: {}", this.index );
	}

	private String getTimeStamp()
	{
		return "[" + this.calendar.getTime() + "] ";
	}
}
